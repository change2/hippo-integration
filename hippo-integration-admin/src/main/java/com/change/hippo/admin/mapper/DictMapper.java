package com.change.hippo.admin.mapper;

import com.change.hippo.admin.entity.DictEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
public interface DictMapper {

	DictEntity get(Long id);

	List<DictEntity> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(DictEntity dict);

	int update(DictEntity dict);

	int remove(Long id);

	int batchRemove(Long[] ids);

	List<DictEntity> listType();
}
