package com.change.hippo.admin.mapper;

import com.change.hippo.admin.entity.UserEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 09:45:11
 */
@Mapper
public interface UserMapper {

	UserEntity get(Long userId);
	
	List<UserEntity> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UserEntity user);
	
	int update(UserEntity user);
	
	int remove(Long userId);
	
	int batchRemove(Long[] userIds);
	
	Long[] listAllDept();

}
