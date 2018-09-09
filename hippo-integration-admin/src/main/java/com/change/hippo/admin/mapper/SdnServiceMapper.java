package com.change.hippo.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.change.hippo.admin.entity.SdnServiceEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface SdnServiceMapper {

    SdnServiceEntity get(Long id);

    List<SdnServiceEntity> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int deleteByPrimaryKey(Long id);

    int batchRemove(Long[] ids);

    int insert(SdnServiceEntity record);

    int insertSelective(SdnServiceEntity record);

    int updateByPrimaryKeySelective(SdnServiceEntity record);

    int updateByPrimaryKey(SdnServiceEntity record);
}