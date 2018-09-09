package com.change.hippo.admin.service.impl;

import com.change.hippo.admin.entity.SdnServiceDomianEntity;
import com.change.hippo.admin.mapper.SdnServiceDomianMapper;
import com.change.hippo.admin.service.DoMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DoMainServiceImpl implements DoMainService {

    @Autowired
    SdnServiceDomianMapper domianMapper;

    @Override
    public SdnServiceDomianEntity get(Long id) {
        return domianMapper.get(id);
    }

    @Override
    public List<SdnServiceDomianEntity> list(Map<String, Object> map) {
        return domianMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return domianMapper.count(map);
    }

    @Override
    public int save(SdnServiceDomianEntity domian) {
        return domianMapper.insert(domian);
    }

    @Override
    public int update(SdnServiceDomianEntity domian) {
        return domianMapper.updateByPrimaryKeySelective(domian);
    }

    @Override
    public int remove(Long id) {
        return domianMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return domianMapper.batchRemove(ids);
    }


    @Override
    public List<SdnServiceDomianEntity> listByName(String name) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("name", name);
        return domianMapper.list(param);
    }

}
