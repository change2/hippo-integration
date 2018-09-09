package com.change.hippo.admin.service.impl;

import com.change.hippo.admin.entity.MenuEntity;
import com.change.hippo.admin.entity.Tree;
import com.change.hippo.admin.mapper.MenuMapper;
import com.change.hippo.admin.mapper.RoleMenuMapper;
import com.change.hippo.admin.utils.BuildTree;
import org.apache.commons.lang3.StringUtils;
import com.change.hippo.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    /**
     * @param
     * @return 树形菜单
     */
    @Cacheable
    @Override
    public Tree<MenuEntity> getSysMenuTree(Long id) {
        List<Tree<MenuEntity>> trees = new ArrayList<Tree<MenuEntity>>();
        List<MenuEntity> menuDOs = menuMapper.listMenuByUserId(id);
        for (MenuEntity sysMenuDO : menuDOs) {
            Tree<MenuEntity> tree = new Tree<MenuEntity>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", sysMenuDO.getUrl());
            attributes.put("icon", sysMenuDO.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<MenuEntity> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public List<MenuEntity> list(Map<String, Object> params) {
        List<MenuEntity> menus = menuMapper.list(params);
        return menus;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int remove(Long id) {
        int result = menuMapper.remove(id);
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int save(MenuEntity menu) {
        int r = menuMapper.save(menu);
        return r;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int update(MenuEntity menu) {
        int r = menuMapper.update(menu);
        return r;
    }

    @Override
    public MenuEntity get(Long id) {
        MenuEntity menuDO = menuMapper.get(id);
        return menuDO;
    }

    @Override
    public Tree<MenuEntity> getTree() {
        List<Tree<MenuEntity>> trees = new ArrayList<Tree<MenuEntity>>();
        List<MenuEntity> menuDOs = menuMapper.list(new HashMap<>(16));
        for (MenuEntity sysMenuDO : menuDOs) {
            Tree<MenuEntity> tree = new Tree<MenuEntity>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<MenuEntity> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Tree<MenuEntity> getTree(Long id) {
        // 根据roleId查询权限
        List<MenuEntity> menus = menuMapper.list(new HashMap<String, Object>(16));
        List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(id);
        List<Long> temp = menuIds;
        for (MenuEntity menu : menus) {
            if (temp.contains(menu.getParentId())) {
                menuIds.remove(menu.getParentId());
            }
        }
        List<Tree<MenuEntity>> trees = new ArrayList<Tree<MenuEntity>>();
        List<MenuEntity> menuDOs = menuMapper.list(new HashMap<String, Object>(16));
        for (MenuEntity sysMenuDO : menuDOs) {
            Tree<MenuEntity> tree = new Tree<MenuEntity>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Map<String, Object> state = new HashMap<>(16);
            Long menuId = sysMenuDO.getMenuId();
            if (menuIds.contains(menuId)) {
                state.put("selected", true);
            } else {
                state.put("selected", false);
            }
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<MenuEntity> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Set<String> listPerms(Long userId) {
        List<String> perms = menuMapper.listUserPerms(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<Tree<MenuEntity>> listMenuTree(Long id) {
        List<Tree<MenuEntity>> trees = new ArrayList<Tree<MenuEntity>>();
        List<MenuEntity> menuDOs = menuMapper.listMenuByUserId(id);
        for (MenuEntity sysMenuDO : menuDOs) {
            Tree<MenuEntity> tree = new Tree<MenuEntity>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", sysMenuDO.getUrl());
            attributes.put("icon", sysMenuDO.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<MenuEntity>> list = BuildTree.buildList(trees, "0");
        return list;
    }

}
