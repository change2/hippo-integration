package com.change.hippo.admin.mapper;

import com.change.hippo.admin.entity.RoleEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 角色
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-02 20:24:47
 */
@Mapper
public interface RoleMapper {

	RoleEntity get(Long roleId);
	
	List<RoleEntity> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RoleEntity role);
	
	int update(RoleEntity role);
	
	int remove(Long roleId);
	
	int batchRemove(Long[] roleIds);
}
