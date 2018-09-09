package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.LogEntity;
import com.change.hippo.admin.entity.PageEntity;
import com.change.hippo.admin.utils.Query;
import org.springframework.stereotype.Service;
@Service
public interface LogService {

	void save(LogEntity logDO);
	PageEntity<LogEntity> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);

}
