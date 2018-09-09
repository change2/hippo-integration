package com.change.hippo.admin.service.impl;

import com.change.hippo.admin.entity.LogEntity;
import com.change.hippo.admin.entity.PageEntity;
import com.change.hippo.admin.mapper.LogMapper;
import com.change.hippo.admin.utils.Query;
import com.change.hippo.admin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogMapper logMapper;

    @Async
    @Override
    public void save(LogEntity logDO) {
        logMapper.save(logDO);
    }

    @Override
    public PageEntity<LogEntity> queryList(Query query) {
        int total = logMapper.count(query);
        List<LogEntity> logs = logMapper.list(query);
        PageEntity<LogEntity> page = new PageEntity<>();
        page.setTotal(total);
        page.setRows(logs);
        return page;
    }

    @Override
    public int remove(Long id) {
        int count = logMapper.remove(id);
        return count;
    }

    @Override
    public int batchRemove(Long[] ids) {
        return logMapper.batchRemove(ids);
    }
}
