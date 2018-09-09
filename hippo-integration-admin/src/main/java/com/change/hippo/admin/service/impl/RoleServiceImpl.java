package com.change.hippo.admin.service.impl;

import com.change.hippo.admin.entity.RoleEntity;
import com.change.hippo.admin.entity.RoleMenuEntity;
import com.change.hippo.admin.mapper.RoleMapper;
import com.change.hippo.admin.mapper.RoleMenuMapper;
import com.change.hippo.admin.mapper.UserMapper;
import com.change.hippo.admin.mapper.UserRoleMapper;
import com.change.hippo.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public List<RoleEntity> list() {
        List<RoleEntity> roles = roleMapper.list(new HashMap<>(16));
        return roles;
    }


    @Override
    public List<RoleEntity> list(Long userId) {
        List<Long> rolesIds = userRoleMapper.listRoleId(userId);
        List<RoleEntity> roles = roleMapper.list(new HashMap<>(16));
        for (RoleEntity roleDO : roles) {
            roleDO.setRoleSign("false");
            for (Long roleId : rolesIds) {
                if (Objects.equals(roleDO.getRoleId(), roleId)) {
                    roleDO.setRoleSign("true");
                    break;
                }
            }
        }
        return roles;
    }

    @Transactional
    @Override
    public int save(RoleEntity role) {
        int count = roleMapper.save(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        List<RoleMenuEntity> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuEntity rmDo = new RoleMenuEntity();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        roleMenuMapper.removeByRoleId(roleId);
        if (rms.size() > 0) {
            roleMenuMapper.batchSave(rms);
        }
        return count;
    }

    @Transactional
    @Override
    public int remove(Long id) {
        int count = roleMapper.remove(id);
        userRoleMapper.removeByRoleId(id);
        roleMenuMapper.removeByRoleId(id);
        return count;
    }

    @Override
    public RoleEntity get(Long id) {
        RoleEntity roleDO = roleMapper.get(id);
        return roleDO;
    }

    @Override
    public int update(RoleEntity role) {
        int r = roleMapper.update(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        roleMenuMapper.removeByRoleId(roleId);
        List<RoleMenuEntity> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuEntity rmDo = new RoleMenuEntity();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        if (rms.size() > 0) {
            roleMenuMapper.batchSave(rms);
        }
        return r;
    }

    @Override
    public int batchremove(Long[] ids) {
        int r = roleMapper.batchRemove(ids);
        return r;
    }

}
