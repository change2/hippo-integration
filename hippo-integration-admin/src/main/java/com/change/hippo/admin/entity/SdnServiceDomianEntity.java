package com.change.hippo.admin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SdnServiceDomianEntity {
    private Long id;

    private String domain;

    private String name;

    private Boolean status;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;

    public SdnServiceDomianEntity() {
    }

    public SdnServiceDomianEntity(SdnServiceDomianEntity domianEntity) {
        this.id = id;
        this.domain = domianEntity.domain;
        this.name = domianEntity.name;
        this.status = true;
        this.createBy = domianEntity.createBy;
        this.createTime = new Date();
    }

}