package com.change.hippo.admin.mapper;

import com.change.hippo.admin.entity.UserRoleEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与角色对应关系
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 11:08:59
 */
@Mapper
public interface UserRoleMapper {

	UserRoleEntity get(Long id);

	List<UserRoleEntity> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserRoleEntity userRole);

	int update(UserRoleEntity userRole);

	int remove(Long id);

	int batchRemove(Long[] ids);

	List<Long> listRoleId(Long userId);

	int removeByUserId(Long userId);

	int removeByRoleId(Long roleId);

	int batchSave(List<UserRoleEntity> list);

	int batchRemoveByUserId(Long[] ids);
}
