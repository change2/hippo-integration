package com.change.hippo.admin.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class RoleEntity {
	
	private Long roleId;
	private String roleName;
	private String roleSign;
	private String remark;
	private Long userIdCreate;
	private Timestamp gmtCreate;
	private Timestamp gmtModified;
	private List<Long> menuIds;
}
