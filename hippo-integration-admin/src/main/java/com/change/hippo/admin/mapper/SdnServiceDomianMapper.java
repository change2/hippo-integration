package com.change.hippo.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.change.hippo.admin.entity.SdnServiceDomianEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface SdnServiceDomianMapper {

    SdnServiceDomianEntity get(Long id);

    List<SdnServiceDomianEntity> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int deleteByPrimaryKey(Long id);

    int batchRemove(Long[] ids);

    int insert(SdnServiceDomianEntity record);

    int insertSelective(SdnServiceDomianEntity record);

    int updateByPrimaryKeySelective(SdnServiceDomianEntity record);

    int updateByPrimaryKey(SdnServiceDomianEntity record);
}