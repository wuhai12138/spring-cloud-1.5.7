package com.sfy.user.service.user;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.sfy.user.config.RabbitConstants;
import com.sfy.user.entity.UserInfo;
import com.sfy.user.form.user.GetAppUsersReq;
import com.sfy.user.form.user.SetUserProfileFrom;
import com.sfy.user.mapper.extend.AppUserExtMapper;
import com.sfy.user.mapper.UserInfoMapper;
import com.sfy.user.dto.PageBean;
import com.sfy.user.dto.jg.JGUserInfo;
import com.sfy.user.dto.user.AppUserDto;
import com.sfy.user.dto.user.UserDto;
import com.sfy.user.dto.user.UserDtoInfo;
import com.sfy.user.utils.ConstantSFY;
import com.sfy.utils.tools.apiResult.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Slf4j
@Service
public class AppUserService {

    @Autowired
    UserService userService;
    @Resource
    UserInfoMapper tUserInfoMapper;

    //    @Autowired
//    JGScheduler jgScheduler;
    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 设置用户资料
     *
     * @param profile
     * @return
     */
    public ApiResult setUserProfile(SetUserProfileFrom profile) {

        UserDtoInfo curUserDtoInfo = userService.getCurUserInfo();

        UserInfo userInfo = tUserInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId,curUserDtoInfo.getId()));
        if (userInfo == null) {
            return ApiResult.error(ConstantSFY.CODE_500, "该用户不存在！");
        }

        if (!StringUtils.isEmpty(profile.getUserCode())){
            userInfo.setUserCode(profile.getUserCode());
        }
        if (!StringUtils.isEmpty(profile.getUserNickname())){
            userInfo.setUserNickname(profile.getUserNickname());
        }
        if (!StringUtils.isEmpty(profile.getUserAddress())){
            userInfo.setUserAddress(profile.getUserAddress());
        }
        if (!StringUtils.isEmpty(profile.getUserIcon())){
            userInfo.setUserIcon(profile.getUserIcon());
        }

        int rows = tUserInfoMapper.updateById(userInfo);

        if (rows > 0) {
            //同步极光
            amqpTemplate.convertAndSend(RabbitConstants.JG_USER_INFO, JSON.toJSONString(new JGUserInfo(curUserDtoInfo.getUsername(), userInfo.getUserNickname(), userInfo.getUserIcon(), System.currentTimeMillis())));
            return ApiResult.success("操作成功！");
        }
        return ApiResult.error(ConstantSFY.CODE_400, "操作失败，请重试！");
    }

    /**
     * 获取App 用户分页列表
     *
     * @param request
     * @return
     */
    public ApiResult<PageBean<AppUserDto>> getPageList(GetAppUsersReq request) {

        int[] total = new int[1];
        List<AppUserDto> list = getList(request, total);
        PageBean<AppUserDto> pageBean = new PageBean<>();
        pageBean.setCurrentPage(request.getCurrentPage());
        pageBean.setPageSize(request.getPageSize());
        pageBean.setTotalCount(total[0]);
        pageBean.setPageData(list);
        return ApiResult.success(pageBean);
    }


    @Resource
    AppUserExtMapper appUserExtMapper;

    /**
     * 根据查询条件获取列表
     *
     * @param request
     * @param total
     * @return
     */
    public List<AppUserDto> getList(GetAppUsersReq request, int[] total) {

        List<AppUserDto> itemList = new ArrayList<>();
        Map<String, Object> search = new LinkedHashMap<>();

        if (request.getId() != null && request.getId() > 0) {
            search.put("id", request.getId());
        }
        if (!Strings.isNullOrEmpty(request.getUsername())) {
            search.put("username", request.getUsername());
        }
        if (!Strings.isNullOrEmpty(request.getEmail())) {
            search.put("email", request.getEmail());
        }
        if (request.getEnabled() != null) {
            search.put("enabled", request.getEnabled());
        }
        if (!Strings.isNullOrEmpty(request.getUserNickname())) {
            search.put("userNickname", request.getUserNickname());
        }
        if (!Strings.isNullOrEmpty(request.getUserCode())) {
            search.put("userCode", request.getUserCode());
        }
        if (!Strings.isNullOrEmpty(request.getRegisterType())) {
            search.put("registerType", request.getRegisterType());
        }
        if (request.getRegBeginTime() != null && request.getRegBeginTime() > 0) {
            search.put("regBeginTime", request.getRegBeginTime());
        }
        if (request.getRegEndTime() != null && request.getRegEndTime() > 0) {
            search.put("regEndTime", request.getRegEndTime());
        }
        total[0] = appUserExtMapper.count(search);
        if (total[0] == 0) {
            return itemList;
        }
        int offset = 0;
        int pageSize = request.getPageSize();
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (request.getCurrentPage() > 1) {
            offset = (request.getCurrentPage() - 1) * pageSize;
        }
        itemList = appUserExtMapper.queryList(search, offset, pageSize, "register_time desc");
        return itemList;
    }

    public ApiResult<Set<AppUserDto>> findAppUserInfoByNames(Set<String> userNames) {
        List<UserDtoInfo> userSet = userService.findUserByNames(userNames).getData();
        if (CollectionUtils.isEmpty(userSet)){
            return ApiResult.error(ConstantSFY.CODE_4000,ConstantSFY.MESSAGE_4000);
        }
        try {
            Set<Long> userIds = userSet.stream().map(UserDto::getId).collect(Collectors.toSet());
            List<AppUserDto> userList = tUserInfoMapper.queryList(userIds);
            if (!CollectionUtils.isEmpty(userList)) {
                Set<AppUserDto> dtoSet = new HashSet<>(userList.size());
                dtoSet.addAll(userList);
//                for (AppUserDto user: userList){
//                    AppUserDto userInfo = new AppUserDto();
//                    BeanUtils.copyProperties(user,userInfo);
//                    dtoSet.add(userInfo);
//                }
                return ApiResult.success(dtoSet);
            }
        } catch (Exception ex) {
            log.error("UserService.findByUsername error:{}", ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_500,ConstantSFY.MESSAGE_500);
    }
}
