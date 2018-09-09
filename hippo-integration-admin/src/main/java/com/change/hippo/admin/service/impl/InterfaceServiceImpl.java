package com.change.hippo.admin.service.impl;

import com.change.hippo.admin.entity.SdnServiceEntity;
import com.change.hippo.admin.mapper.SdnServiceMapper;
import com.change.hippo.admin.service.InterfaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class InterfaceServiceImpl implements InterfaceService {

    @Resource
    SdnServiceMapper sdnServiceMapper;

    @Override
    public SdnServiceEntity get(Long id) {
        return sdnServiceMapper.get(id);
    }

    @Override
    public List<SdnServiceEntity> list(Map<String, Object> map) {
        return sdnServiceMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return sdnServiceMapper.count(map);
    }

    @Override
    public int save(SdnServiceEntity service) {
        return sdnServiceMapper.insert(service);
    }

    @Override
    public int update(SdnServiceEntity service) {
        return sdnServiceMapper.updateByPrimaryKeySelective(service);
    }

    @Override
    public int remove(Long id) {
        return sdnServiceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return sdnServiceMapper.batchRemove(ids);
    }

}
