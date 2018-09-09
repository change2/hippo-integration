package com.change.hippo.admin.mapper;

import com.change.hippo.admin.entity.LogEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
public interface LogMapper {

	LogEntity get(Long id);
	
	List<LogEntity> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(LogEntity log);
	
	int update(LogEntity log);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
