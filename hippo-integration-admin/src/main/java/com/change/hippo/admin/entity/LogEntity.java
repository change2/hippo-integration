package com.change.hippo.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LogEntity {
	private Long id;

	private Long userId;

	private String username;

	private String operation;

	private Integer time;

	private String method;

	private String params;

	private String ip;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date gmtCreate;
}