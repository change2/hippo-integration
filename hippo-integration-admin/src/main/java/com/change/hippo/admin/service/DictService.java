package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.DictEntity;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */
public interface DictService {
	
	DictEntity get(Long id);
	
	List<DictEntity> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DictEntity dict);
	
	int update(DictEntity dict);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<DictEntity> listType();
	
	String getName(String type, String value);

	/**
	 * 根据type获取数据
	 * @param type
	 * @return
	 */
	List<DictEntity> listByType(String type);

}
