package com.change.hippo.admin.mapper;

import com.change.hippo.admin.entity.DeptEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 部门管理
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:35:39
 */
@Mapper
public interface DeptMapper {

	DeptEntity get(Long deptId);
	
	List<DeptEntity> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(DeptEntity dept);
	
	int update(DeptEntity dept);
	
	int remove(Long deptId);
	
	int batchRemove(Long[] deptIds);
	
	Long[] listParentDept();
	
	int getDeptUserNumber(Long deptId);
}
