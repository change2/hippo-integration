package com.change.hippo.admin.mapper;

import com.change.hippo.admin.entity.RoleMenuEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与菜单对应关系
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 11:08:59
 */
@Mapper
public interface RoleMenuMapper {

	RoleMenuEntity get(Long id);
	
	List<RoleMenuEntity> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RoleMenuEntity roleMenu);
	
	int update(RoleMenuEntity roleMenu);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	List<Long> listMenuIdByRoleId(Long roleId);
	
	int removeByRoleId(Long roleId);

	int removeByMenuId(Long menuId);
	
	int batchSave(List<RoleMenuEntity> list);
}
