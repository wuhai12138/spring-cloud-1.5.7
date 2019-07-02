package com.sfy.user.service.meun;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sfy.user.constant.UserConstant;
import com.sfy.user.entity.SysMenu;
import com.sfy.user.entity.SysRoleMenu;
import com.sfy.user.mapper.extend.SysRoleMenuExtMapper;
import com.sfy.user.form.menu.SetMenuForm;
import com.sfy.user.form.menu.SetMenusHideForm;
import com.sfy.user.mapper.SysMenuMapper;
import com.sfy.user.mapper.SysRoleMenuMapper;
import com.sfy.user.dto.menu.MenuDto;
import com.sfy.user.utils.DozerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@Slf4j
@Service
public class MenuService {

//    @Autowired
    @Resource
    SysMenuMapper sysMenuMapper;

//    @Autowired
    @Resource
    SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取菜单信息
     *
     * @param menuId
     * @return
     */
    public SysMenu get(Integer menuId) {
        return sysMenuMapper.selectOne(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getId, menuId).eq(SysMenu::getHidden, 0));
    }

    /**
     * 获取所有系统分类(hide 是否隐藏)
     *
     * @return
     */
    public List<SysMenu> getAll(Byte hide) {
        if (hide == null) {
            return sysMenuMapper.selectList(Wrappers.lambdaQuery());
        }

        return sysMenuMapper.selectList(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getHidden, hide));
    }


    /**
     * 设置菜单
     *
     * @param request
     * @return
     */
    public SysMenu set(SetMenuForm request) {

        if (request.getSort() == null) {
            request.setSort(100);
        }
        if (request.getId() != null && request.getId() > 0) {
            //update
            SysMenu sysMenu = get(request.getId());
            if (sysMenu == null) {
                return null;
            }
            sysMenu = DozerMapper.map(request, SysMenu.class);
            sysMenu.setHidden(0);
            if (sysMenuMapper.updateById(sysMenu) > 0) {
                removeFromRedisBy(Arrays.asList(sysMenu.getId()));
                return sysMenu;
            }
        } else {
            //insert
            SysMenu sysMenu = new SysMenu();
            sysMenu = DozerMapper.map(request, SysMenu.class);
            sysMenu.setHidden(0);
            if (sysMenuMapper.insert(sysMenu) > 0) {
                return sysMenu;
            }
        }
        return null;
    }

    /**
     * 批量操作菜单 是否隐藏
     *
     * @param request
     * @return
     */
    @Transactional
    public boolean batchHide(SetMenusHideForm request) throws Exception {

        if (CollectionUtils.isEmpty(request.getMenuIds())) {
            return false;
        }
        if (request.getHidden() == null || request.getHidden() != 1) {
            request.setHidden((byte) 0);
        }

        try {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setHidden(request.getHidden().intValue());
            if (sysMenuMapper.update(sysMenu,Wrappers.<SysMenu>lambdaUpdate().in(SysMenu::getId,request.getMenuIds())) > 0) {
                //设置成功
                removeFromRedisBy(request.getMenuIds());
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 获取角色菜单信息
     *
     * @param roleId
     * @return
     */
    public List<SysMenu> getMenusByRoleId(Long roleId) {

        List<SysRoleMenu> entities = sysRoleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }

        List<Integer> menuIds = new ArrayList<>();
        entities.parallelStream().forEach(entity -> {
            menuIds.add(entity.getMenuId());
        });

        return sysMenuMapper.selectList(Wrappers.<SysMenu>lambdaQuery().in(SysMenu::getId,menuIds).eq(SysMenu::getHidden,0));
    }

    /**
     * 从缓存中获取角色菜单信息
     *
     * @param roleId
     * @return
     */
    public Map<Integer, SysMenu> getMapByRoleId(Long roleId) {

        Map<Integer, SysMenu> menuMap = null;
        HashOperations<String, Integer, SysMenu> hashOperations = redisTemplate.opsForHash();
        String redisKey = UserConstant.getRoleMeunsKey(roleId);
        if (redisTemplate.hasKey(redisKey)) {

            menuMap = hashOperations.entries(redisKey);
        }
        if (menuMap == null) {
            menuMap = new HashMap<>();
        }
        if (menuMap.size() == 0) {
            List<SysMenu> list = getMenusByRoleId(roleId);
            if (list != null && list.size() > 0) {
                for (SysMenu entity : list) {
                    menuMap.put(entity.getId(), entity);
                }
                hashOperations.putAll(redisKey, menuMap);
                redisTemplate.expire(redisKey, UserConstant.getRedisTimeOut(), TimeUnit.SECONDS);
            }
        }
        return menuMap;
    }

    /**
     * 获取多个角色的菜单信息
     *
     * @param roleIds
     * @return
     */
    public Map<Integer, SysMenu> getMapByRoleIds(List<Long> roleIds) {

        Map<Integer, SysMenu> menuMap = new HashMap<>();

        if (CollectionUtils.isEmpty(roleIds)) {
            return menuMap;
        }
        roleIds.parallelStream().forEach(roleId -> {
            menuMap.putAll(getMapByRoleId(roleId));
        });
        return menuMap;
    }

    /**
     * 根据菜单信息组装树形结构
     *
     * @param sysMenus
     * @return
     */
    public List<MenuDto> builderByList(Collection<SysMenu> sysMenus) {
        return builderByList(sysMenus, false);
    }

    /**
     * 根据菜单信息组装树形结构
     *
     * @param sysMenus
     * @param withAll  是否将系统菜单带出
     * @return
     */
    public List<MenuDto> builderByList(Collection<SysMenu> sysMenus, boolean withAll) {

        Collection<SysMenu> sourceList = sysMenus;
        if (withAll) {
            sourceList = getAll((byte) 0);
        }

        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        return builderToTree(sourceList, sysMenus);
    }

    /**
     * 组装树形结构
     *
     * @param source
     * @param checked
     * @return
     */
    private List<MenuDto> builderToTree(Collection<SysMenu> source, Collection<SysMenu> checked) {

        Set<Integer> checkedIds = new HashSet<>();
        if (checked != null && checked.size() > 0) {
            checked.parallelStream().forEach(item -> {
                checkedIds.add(item.getId());
            });
        }
        //to tree 用递归找
        List<MenuDto> treeList = new ArrayList<MenuDto>();
        for (SysMenu menu : source) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                treeList.add(findChildren(menu, source, checkedIds, 1));
            }
        }
        return treeList;
    }

    /**
     * 递归查找子类
     *
     * @param menu
     * @param source
     * @param checkedIds
     * @return
     */
    private MenuDto findChildren(SysMenu menu, Collection<SysMenu> source, Set<Integer> checkedIds, int depth) {

        MenuDto menuDto = DozerMapper.map(menu, MenuDto.class);
        if (checkedIds.contains(menu.getId())) {
            menuDto.setChecked(1);
        }

        if (depth <= 3) {
            //控制深度，防止数据贪吃
            for (SysMenu sub : source) {
                if (sub.getParentId().compareTo(menu.getId()) == 0) {
                    if (menuDto.getSubList() == null) {
                        menuDto.setSubList(new ArrayList<>());
                    }
                    menuDto.getSubList().add(findChildren(sub, source, checkedIds, depth++));
                }
            }
        }
        return menuDto;
    }

    @Autowired
    SysRoleMenuExtMapper sysRoleMenuExtMapper;

    private void removeFromRedisBy(List<Integer> menuIds) {

        List<Long> roleIds = sysRoleMenuExtMapper.getRoleIds(menuIds);
        if (roleIds != null && roleIds.size() > 0) {
            roleIds.parallelStream().forEach(roleId -> {
                removeFromRedis(roleId);
            });
        }
    }

    /**
     * 从缓存中删除所有角色对应的菜单信息
     */
    private void removeAllFromRedis() {
        String redisKey = UserConstant.Role_Meuns_All_Key;
        Set<String> keys = redisTemplate.keys(redisKey);
        redisTemplate.delete(keys);
    }

    /**
     * 从缓存中删除指定角色对应的菜单信息
     *
     * @param roleId
     */
    private void removeFromRedis(Long roleId) {

        String redisKey = UserConstant.getRoleMeunsKey(roleId);
        if (redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(redisKey);
        }
    }
}