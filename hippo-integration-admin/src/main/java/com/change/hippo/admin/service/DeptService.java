package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.DeptEntity;
import com.change.hippo.admin.entity.Tree;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:28:36
 */
public interface DeptService {
	
	DeptEntity get(Long deptId);
	
	List<DeptEntity> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DeptEntity sysDept);
	
	int update(DeptEntity sysDept);
	
	int remove(Long deptId);
	
	int batchRemove(Long[] deptIds);

	Tree<DeptEntity> getTree();
	
	boolean checkDeptHasUser(Long deptId);
}
