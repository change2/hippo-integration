package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.SdnServiceEntity;

import java.util.List;
import java.util.Map;

public interface InterfaceService {

    SdnServiceEntity get(Long id);

    List<SdnServiceEntity> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(SdnServiceEntity entity);

    int update(SdnServiceEntity entity);

    int remove(Long id);

    int batchRemove(Long[] ids);

}
