package com.sfy.user.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.sfy.boot.exception.SFYException;
import com.sfy.user.entity.*;
import com.sfy.user.form.user.GetSysUsersReq;
import com.sfy.user.form.user.SetSysUserForm;
import com.sfy.user.mapper.SysUserInfoMapper;
import com.sfy.user.mapper.UserMapper;
import com.sfy.user.mapper.UserRolesMapper;
import com.sfy.user.dto.menu.MenuDto;
import com.sfy.user.dto.role.AuthorityDto;
import com.sfy.user.dto.user.SysUserDto;
import com.sfy.user.dto.user.UserRoleDto;
import com.sfy.user.service.meun.MenuService;
import com.sfy.user.service.role.RoleService;
import com.sfy.user.utils.ConstantSFY;
import com.sfy.user.utils.DozerMapper;
import com.sfy.utils.tools.apiResult.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Slf4j
@Service
public class SysUserService {

    @Autowired
    Md5PasswordEncoder passwordEncoder;
    @Autowired
    UserRolesMapper userRolesMapper;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SysUserInfoMapper sysUserInfoMapper;

    /**
     * 根据用户ID获取系统用户
     *
     * @param userId
     * @return
     */
    public SysUserInfo getByUserId(Long userId) {

        return sysUserInfoMapper.selectOne(Wrappers.<SysUserInfo>lambdaQuery().eq(SysUserInfo::getUserId, userId));
    }

    /**
     * 根据ID号更新用户信息
     *
     * @param sysUserInfo
     * @return
     */
    public boolean updateById(SysUserInfo sysUserInfo) {
        return sysUserInfoMapper.updateById(sysUserInfo) > 0;
    }

    /**
     * 设置用户信息 -- 预留超级管理员无法修改
     *
     * @param form
     * @return
     */
    @Transactional
    public ApiResult<SysUserInfo> setSysUser(SetSysUserForm form) throws SFYException {

        SysUserInfo sysUserInfo = null;
        User user = null;
        if (form.getUserId() != null && form.getUserId() > 0) {
            //update
            if (form.getUserId() == 1) {
                return ApiResult.error(ConstantSFY.CODE_400, "预留超级管理员账号无法修改");
            }
            user = userService.get(form.getUserId());
            if (user == null) {
                return ApiResult.error(ConstantSFY.CODE_400, "用户不存在");
            }
            sysUserInfo = getByUserId(form.getUserId());
            if (sysUserInfo == null) {
                return ApiResult.error(ConstantSFY.CODE_400, "该用户不是系统用户！");
            }

            //sysUserInfo.setSysUsername(form.getSysUsername());
            sysUserInfo.setShopId(form.getShopId());
            sysUserInfo.setShopName(form.getShopName());
            sysUserInfo.setAccountNumber(form.getAccountNumber());
            sysUserInfo.setUpdateTime(new Date());
            if (sysUserInfoMapper.updateById(sysUserInfo) <= 0) {
                throw new SFYException(ConstantSFY.CODE_400, "修改系统用户信息失败！");
            }

            if (!Strings.isNullOrEmpty(form.getPassword())) {
                user.setPassword(passwordEncoder.encodePassword(form.getPassword(), ConstantSFY.PASSWORD_SALT));
            }
            if (!Strings.isNullOrEmpty(form.getEmail()))
                user.setEmail(form.getEmail());
            if (!Strings.isNullOrEmpty(form.getImageUrl()))
                user.setImageUrl(form.getImageUrl());
            if (userMapper.updateById(user) <= 0) {
                throw new SFYException(ConstantSFY.CODE_400, "更新用户信息失败！");
            }

        } else {
            //check user name

            Integer count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getUsername, form.getUsername()).or().eq(User::getMobile, form.getUsername()));
            if (count > 0) {
                return ApiResult.error(ConstantSFY.CODE_400, "该用户名/手机号已经存在！");
            }

            user = new User();
            user.setUsername(form.getUsername());
            user.setPassword(passwordEncoder.encodePassword(form.getPassword(), ConstantSFY.PASSWORD_SALT));
            user.setEmail(form.getEmail());
            user.setImageUrl(form.getImageUrl());
            user.setEnabled(1);
            user.setPasswordRevised(1);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());

            if (userMapper.insert(user) <= 0) {
                return ApiResult.error(ConstantSFY.CODE_400, "创建用户失败！");
            }

            sysUserInfo = new SysUserInfo();
            sysUserInfo.setUserId(user.getId());
            sysUserInfo.setSysUsername(form.getUsername());
            sysUserInfo.setAccountNumber(form.getAccountNumber());
            sysUserInfo.setShopId(form.getShopId());
            sysUserInfo.setShopName(form.getShopName());
            sysUserInfo.setCreateTime(new Date());
            sysUserInfo.setUpdateTime(new Date());
            if (sysUserInfoMapper.insert(sysUserInfo) <= 0) {
                throw new SFYException(ConstantSFY.CODE_400, "创建系统用户失败！");
            }
        }
        return ApiResult.success(sysUserInfo);
    }


    /**
     * 获取系统用户信息
     * @param userId
     * @return
     */
    public SysUserDto getSysUser(Long userId) {

        User user = userService.get(userId);
        if (user == null) return null;
        SysUserInfo sysUserInfo = getByUserId(userId);
        if (sysUserInfo == null) return null;
        SysUserDto sysUserDto = DozerMapper.map(user, SysUserDto.class);
        sysUserDto.setShopId(sysUserInfo.getShopId());
        sysUserDto.setShopName(sysUserInfo.getShopName());
        sysUserDto.setIsLogin(sysUserInfo.getIsLogin());
        sysUserDto.setLoginTime(sysUserInfo.getLoginTime());
        sysUserDto.setSysUsername(sysUserInfo.getSysUsername());
        sysUserDto.setAccountNumber(sysUserInfo.getAccountNumber());
        return sysUserDto;
    }
    /**
     * 获取系统用户角色列表
     *
     * @param request
     * @return
     */
    public ApiResult<Page<SysUserDto>> getPageList(GetSysUsersReq request) {

        try {
            IPage<SysUserInfo> page = new Page<>();
            page.setCurrent(request.getCurrentPage());
            page.setSize(request.getPageSize());
            IPage<SysUserInfo> sysUserInfoIPage = sysUserInfoMapper.selectPage(page, Wrappers.<SysUserInfo>lambdaQuery()
                    .like(!Strings.isNullOrEmpty(request.getSysUserName()), SysUserInfo::getSysUsername, request.getSysUserName())
                    .eq(!Strings.isNullOrEmpty(request.getAccountNumber()), SysUserInfo::getAccountNumber, request.getAccountNumber())
                    .eq((request.getShopId() != null && request.getShopId() > 0), SysUserInfo::getShopId, request.getShopId())
                    .orderByDesc(SysUserInfo::getId));

            //get user map
            if (sysUserInfoIPage.getRecords() != null && sysUserInfoIPage.getRecords().size() > 0) {
                List<Long> userIds = new ArrayList<>();
                sysUserInfoIPage.getRecords().parallelStream().forEach(sysUserInfo -> {
                    userIds.add(sysUserInfo.getUserId());
                });
                Map<Long, User> userMap = userService.getUserMapByIds(userIds);
                Page<SysUserDto> sysUserDtoIPage = new Page<>();
                sysUserDtoIPage.setSize(sysUserInfoIPage.getSize());
                sysUserDtoIPage.setCurrent(sysUserDtoIPage.getCurrent());
                sysUserDtoIPage.setPages(sysUserDtoIPage.getPages());
                sysUserDtoIPage.setTotal(sysUserDtoIPage.getTotal());
                List<SysUserDto> records = new ArrayList<>();
                for(SysUserInfo sysUserInfo:sysUserInfoIPage.getRecords()){

                    if (userMap.containsKey(sysUserInfo.getUserId())) {
                        SysUserDto sysUserDto = DozerMapper.map(userMap.get(sysUserInfo.getUserId()), SysUserDto.class);
                        sysUserDto.setShopId(sysUserInfo.getShopId());
                        sysUserDto.setShopName(sysUserInfo.getShopName());
                        sysUserDto.setIsLogin(sysUserInfo.getIsLogin());
                        sysUserDto.setLoginTime(sysUserInfo.getLoginTime());
                        sysUserDto.setSysUsername(sysUserInfo.getSysUsername());
                        sysUserDto.setAccountNumber(sysUserInfo.getAccountNumber());
                        records.add(sysUserDto);
                    }
                }
                sysUserDtoIPage.setRecords(records);
                return ApiResult.success(sysUserDtoIPage);
            }
            return ApiResult.success(null);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new SFYException(ConstantSFY.CODE_500, ConstantSFY.MESSAGE_500);
        }
    }


    //region ==========================================系统用户===================================

    /**
     * 添加用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    public boolean addUserRole(Long userId, Long roleId) {

        try {
            if (userId == null || userId == 0) {
                return false;
            }
            if (roleId == null || roleId == 0) {
                return false;
            }
            User user = userService.get(userId);
            if (user == null || user.getEnabled() == null || user.getEnabled() == 0) {
                return false;
            }

            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(userId);
            userRoles.setRolesId(roleId);
            Integer count = userRolesMapper.selectCount(Wrappers.<UserRoles>lambdaQuery().eq(UserRoles::getUserId, userId).eq(UserRoles::getRolesId, roleId));
            if (count == 0) {
                userRolesMapper.insert(userRoles);
            }

            return true;
        } catch (Exception ex) {
            log.error("UserService.setUserAuths error:{}", ex.getMessage());
        }
        return false;
    }

    /**
     * 设置/清空 用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setUserRoles(Long userId, List<Long> roleIds) {

        try {
            if (userId == null || userId == 0) {
                return false;
            }
            User user = userService.get(userId);
            if (user == null || user.getEnabled() == null || user.getEnabled() == 0) {
                return false;
            }
            //清空用户角色
            userRolesMapper.delete(Wrappers.<UserRoles>lambdaUpdate().eq(UserRoles::getUserId, userId));

            if (!CollectionUtils.isEmpty(roleIds)) {
                UserRoles userRoles = new UserRoles();
                userRoles.setUserId(userId);
                for (Long roleId : roleIds) {
                    userRoles.setRolesId(roleId);
                    Integer count = userRolesMapper.selectCount(Wrappers.<UserRoles>lambdaQuery().eq(UserRoles::getRolesId, roleId));
                    if (count == 0) {
                        userRolesMapper.insert(userRoles);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("UserService.setUserAuths error:{}", ex.getMessage());
        }
        return false;
    }

    /**
     * 获取用户选中的角色信息
     *
     * @param userId
     * @return
     */
    public List<UserRoleDto> getUserSelectedRoles(Long userId) {

        List<UserRoleDto> dtoList = new ArrayList<>();
        List<Role> all = roleService.getAll();
        if (all == null || all.size() == 0) {
            return dtoList;
        }

        List<Role> userRoles = roleService.getRoles(userId);
        Set<Long> roleIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(userRoles)) {
            userRoles.parallelStream().forEach(userRole -> {
                roleIds.add(userRole.getId());
            });
        }

        for (Role role : all) {
            UserRoleDto roleDto = DozerMapper.map(role, UserRoleDto.class);
            if (roleIds.contains(roleDto.getId())) {
                roleDto.setChecked(1);
            }
            dtoList.add(roleDto);
        }
        return dtoList;
    }

    /**
     * 获取用户选中的菜单信息
     *
     * @param userId
     * @return
     */
    public List<MenuDto> getUserSelectedMenus(Long userId) {

        List<MenuDto> menuDtos = new ArrayList<>();
        List<Long> roleIds = userService.getRoleIds(userId);
        if (roleIds == null || roleIds.size() == 0) {
            return menuDtos;
        }

        Map<Integer, SysMenu> menuMap = menuService.getMapByRoleIds(roleIds);
        if (menuMap == null || menuMap.size() == 0) {
            return menuDtos;
        }

        return menuService.builderByList(menuMap.values(), true);
    }


    /**
     * 获取用户菜单
     */
    public List<MenuDto> getUserMenus(Long userId) {

        List<MenuDto> menuDtos = new ArrayList<>();
        List<Long> roleIds = userService.getRoleIds(userId);
        if (roleIds == null || roleIds.size() == 0) {
            return menuDtos;
        }

        Map<Integer, SysMenu> menuMap = menuService.getMapByRoleIds(roleIds);
        if (menuMap == null || menuMap.size() == 0) {
            return menuDtos;
        }

        return menuService.builderByList(menuMap.values());
    }

    /**
     * 获取用户权限列表
     *
     * @param userId
     * @return
     */
    public List<Authority> getUserAuths(Long userId) {

        List<Authority> authorities = new ArrayList<>();
        List<Long> roleIds = userService.getRoleIds(userId);
        if (roleIds == null || roleIds.size() == 0) {
            return authorities;
        }

        Map<Long, Authority> authorityMap = roleService.getAuthMapByRoleIds(roleIds);
        if (authorityMap == null || authorityMap.size() == 0) {
            return authorities;
        }
        return new ArrayList<>(authorityMap.values());
    }

    /**
     * 获取用户选中的权限信息
     *
     * @param userId
     * @return
     */
    public List<AuthorityDto> getUserSelectedAuths(Long userId) {

        List<AuthorityDto> authorities = new ArrayList<>();
        List<Long> roleIds = userService.getRoleIds(userId);
        if (roleIds == null || roleIds.size() == 0) {
            return authorities;
        }

        return roleService.getAuthDtosByRoleIds(roleIds);
    }

    //endregion ==================================================================================
}