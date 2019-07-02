package com.sfy.user.service.role;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.sfy.boot.exception.SFYException;
import com.sfy.user.constant.UserConstant;
import com.sfy.user.entity.*;
import com.sfy.user.form.role.GetRolesPageReq;
import com.sfy.user.form.role.SetRoleFrom;
import com.sfy.user.mapper.*;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.role.AuthorityDto;
import com.sfy.user.dto.role.RoleDto;
import com.sfy.user.service.meun.MenuService;
import com.sfy.user.utils.ConstantSFY;
import com.sfy.user.utils.DozerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@Slf4j
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Autowired
    private RoleAuthoritiesMapper roleAuthoritiesMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取角色数据模型
     *
     * @param roleId
     * @param withAuthority
     * @param withMenu
     * @return
     */
    public RoleDto getRoleDto(Long roleId, boolean withAuthority, boolean withMenu) {

        RoleDto roleDto = new RoleDto();
        Role role = get(roleId);
        if (role == null) {
            return roleDto;
        }
        roleDto = DozerMapper.map(role, RoleDto.class);
        if (withAuthority) {
            roleDto.setAuthorities(getAuthDtosByRoleIds(Arrays.asList(roleId)));
        }
        if (withMenu) {
            List<SysMenu> sysMenus = new ArrayList<>(menuService.getMapByRoleId(roleId).values());
            roleDto.setMenus(menuService.builderByList(sysMenus, true));
        }
        return roleDto;
    }

    /**
     * 设置角色信息
     *
     * @param request
     * @return
     */
    public Role set(SetRoleFrom request) {

        if (request.getId() != null && request.getId() > 0) {
            //update
            Role role = get(request.getId());
            if (role == null) {
                return null;
            }
            BeanUtils.copyProperties(request,role);
            role.setUpdateTime(new Date());
            if (roleMapper.updateById(role) > 0) {
                return role;
            }
        } else {
            //insert
            Role role = new Role();
            BeanUtils.copyProperties(request,role);
            role.setCreateTime(new Date());
            role.setUpdateTime(new Date());
            if (roleMapper.insert(role) > 0) {
                return role;
            }
        }
        return null;
    }

    /**
     * 设置角色权限
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setRoleAuthorities(Long roleId, List<Long> authIds) throws Exception {

        Role role = get(roleId);
        if (role == null) {
            return false;
        }
        try {
            //删除角色对应的权限
            roleAuthoritiesMapper.delete(Wrappers.<RoleAuthorities>lambdaUpdate().eq(RoleAuthorities::getRoleId,role.getId()));
            if (!CollectionUtils.isEmpty(authIds)) {
                RoleAuthorities roleAuthorities = new RoleAuthorities();
                roleAuthorities.setRoleId(roleId);
                for (Long authId : authIds) {
                    roleAuthorities.setAuthoritiesId(authId);
                    if (roleAuthoritiesMapper.insert(roleAuthorities) <= 0) {
                        throw new Exception(JSON.toJSONString(roleAuthorities));
                    }
                }
            }
            removeAuthsFromRedis(roleId);
            return true;
        } catch (Exception ex) {
            log.error("RoleService.setRoleAuthorities error:{}", ex.getMessage());
            throw ex;
        }
    }

    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * 批量设置角色菜单
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setRoleMenus(Long roleId, List<Integer> menuIds) throws Exception {

        if (roleId == null || roleId == 0) {
            return false;
        }
        try {
            sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaUpdate().eq(SysRoleMenu::getRoleId,roleId));
            if (!CollectionUtils.isEmpty(menuIds)) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                for (Integer menuId : menuIds) {
                    sysRoleMenu.setRoleId(roleId);
                    sysRoleMenu.setMenuId(menuId);
                    if (sysRoleMenuMapper.insert(sysRoleMenu) <= 0) {
                        throw new Exception(JSON.toJSONString(sysRoleMenu));
                    }
                }
            }
            //设置成功
            removeMenusFromRedis(roleId);
            return true;
        } catch (Exception ex) {
            log.error("RoleService.setRoleMenus error:{}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * 获取角色信息
     *
     * @param id
     * @return
     */
    public Role get(Long id) {
        return roleMapper.selectById(id);
    }

    /**
     * 获取所有角色
     *
     * @return
     */
    public List<Role> getAll() {
        return roleMapper.selectList(Wrappers.lambdaQuery());
    }

    /**
     * 获取所有权限
     *
     * @return
     */
    public List<Authority> getAllAuthorities() {
        return authorityMapper.selectList(Wrappers.lambdaQuery());
    }

    /**
     * 获取用户角色
     *
     * @param roleIds
     * @return
     */
    public List<Role> getRoles(List<Long> roleIds) {

        List<Role> roles = new ArrayList<>();
        try {
            roles = roleMapper.selectList(Wrappers.<Role>lambdaQuery().in(Role::getId, roleIds));
        } catch (Exception ex) {
            log.error("RoleService.findRolesByUserId error: {}", ex.getMessage());
        }
        return roles;
    }


    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    public List<Role> getRoles(Long userId) {

        List<Role> roles = new ArrayList<>();
        try {
            List<UserRoles> entities = userRolesMapper.selectList(Wrappers.<UserRoles>lambdaQuery().eq(UserRoles::getUserId,userId));
            if (entities == null || entities.size() == 0) {
                return roles;
            }

            List<Long> ids = new ArrayList<>();
            entities.parallelStream().forEach(entity -> {
                ids.add(entity.getRolesId());
            });

            roles = roleMapper.selectList(Wrappers.<Role>lambdaQuery().in(Role::getId,ids));
        } catch (Exception ex) {
            log.error("RoleService.getRoles error: {}", ex.getMessage());
        }
        return roles;
    }

    /**
     * 获取角色分页列表
     * @param request
     * @return
     */
    public ApiResult<Page<Role>> getPageList(GetRolesPageReq request) {

        try {
            Page<Role> page = new Page<>();
            page.setCurrent(request.getCurrentPage());
            page.setSize(request.getPageSize());
            IPage<Role> roleIPage = roleMapper.selectPage(page, Wrappers.<Role>lambdaQuery()
                    .like(!Strings.isNullOrEmpty(request.getName()), Role::getName, request.getName())
                    .eq(request.getId() != null && request.getId() > 0, Role::getId, request.getId())
                    .orderByAsc(Role::getCreateTime));
            page.setRecords(roleIPage.getRecords());
            return ApiResult.success(page);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new SFYException(ConstantSFY.CODE_500, ConstantSFY.MESSAGE_500);
        }
    }
    /**
     * 获取角色权限
     *
     * @param roleId
     * @return
     */
    public List<Authority> getAuthorities(Long roleId) {

        List<Authority> authorities = new ArrayList<>();
        try {
            List<RoleAuthorities> entities = roleAuthoritiesMapper.selectList(Wrappers.<RoleAuthorities>lambdaQuery().eq(RoleAuthorities::getRoleId,roleId));
            //authorities
            if (CollectionUtils.isEmpty(entities)) {
                return authorities;
            }

            List<Long> ids = new ArrayList<>();
            entities.parallelStream().forEach(entity -> {
                ids.add(entity.getAuthoritiesId());
            });

            authorities = authorityMapper.selectList(Wrappers.<Authority>lambdaQuery().in(Authority::getId,ids));
        } catch (Exception ex) {
            log.error("RoleService.getAuthorities error: {}", ex.getMessage());
        }
        return authorities;
    }

    /**
     * 获取角色权限Map
     *
     * @param roleId
     * @return
     */
    public Map<Long, Authority> getMapByByRoleId(Long roleId) {

        Map<Long, Authority> authorityMap = null;
        try {
            HashOperations<String, Long, Authority> hashOperations = redisTemplate.opsForHash();
            String redisKey = UserConstant.getRoleAuthoritiesKey(roleId);
            if (redisTemplate.hasKey(redisKey)) {

                authorityMap = hashOperations.entries(redisKey);
            }
            if (authorityMap == null) {
                authorityMap = new HashMap<>();
            }
            if (authorityMap.size() == 0) {
                List<Authority> list = getAuthorities(roleId);
                if (list != null && list.size() > 0) {
                    for (Authority entity : list) {
                        authorityMap.put(entity.getId(), entity);
                    }
                    hashOperations.putAll(redisKey, authorityMap);
                    redisTemplate.expire(redisKey, UserConstant.getRedisTimeOut(), TimeUnit.SECONDS);
                }
            }
        } catch (Exception ex) {
            log.error("RoleService.getMapByByRoleId error: {}", ex.getMessage());
        }
        return authorityMap;
    }

    /**
     * 根据用户角色ID列表获取权限
     *
     * @param roleIds
     * @return
     */
    public Map<Long, Authority> getAuthMapByRoleIds(List<Long> roleIds) {

        Map<Long, Authority> authorityMap = new HashMap<>();
        try {
            if (CollectionUtils.isEmpty(roleIds)) {
                return authorityMap;
            }
            roleIds.parallelStream().forEach(roleId -> {
                authorityMap.putAll(getMapByByRoleId(roleId));
            });
        } catch (Exception ex) {
            log.error("RoleService.getAuthoritiesByRoleIds error: {}", ex.getMessage());
        }
        return authorityMap;
    }

    /**
     * 获取指定角色ID列表对应的权限集合
     *
     * @param roleIds
     * @return
     */
    public List<AuthorityDto> getAuthDtosByRoleIds(List<Long> roleIds) {

        List<AuthorityDto> authorityDtos = new ArrayList<>();

        List<Authority> authorities = getAllAuthorities();
        Map<Long, Authority> authorityMap = getAuthMapByRoleIds(roleIds);
        if (authorities == null || authorities.size() == 0) {
            return authorityDtos;
        }
        authorityDtos = DozerMapper.mapList(authorities, AuthorityDto.class);
        for (AuthorityDto authorityDto : authorityDtos) {
            if (authorityMap.containsKey(authorityDto.getId())) {
                authorityDto.setChecked(1);
            }
        }
        return authorityDtos;
    }

    /**
     * 从缓存中删除指定角色对应的权限
     *
     * @param roleId
     */
    private void removeAuthsFromRedis(Long roleId) {

        String redisKey = UserConstant.getRoleAuthoritiesKey(roleId);
        if (redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(redisKey);
        }
    }

    private void removeMenusFromRedis(Long roleId) {

        String redisKey = UserConstant.getRoleMeunsKey(roleId);
        if (redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(redisKey);
        }
    }
}