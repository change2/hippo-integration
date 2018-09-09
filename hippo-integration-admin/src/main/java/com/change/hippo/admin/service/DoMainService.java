package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.SdnServiceDomianEntity;

import java.util.List;
import java.util.Map;

public interface DoMainService {

    SdnServiceDomianEntity get(Long id);

    List<SdnServiceDomianEntity> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(SdnServiceDomianEntity dict);

    int update(SdnServiceDomianEntity dict);

    int remove(Long id);

    int batchRemove(Long[] ids);

    /**
     * 根据name获取数据
     * @param name
     * @return
     */
    List<SdnServiceDomianEntity> listByName(String name);

}
