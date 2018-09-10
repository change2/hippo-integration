package com.change.hippo.admin.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PageEntity<T> {

	private int offset;
	private int limit;
	private int total;
	private Map<String, Object> params;
	private String param;
	private List<T> rows;

	public PageEntity() {
		super();
		this.offset = 0;
		this.limit = 10;
		this.total = 1;
		this.params = new HashMap<>();
		this.param = "";
		this.rows = new ArrayList<>();
	}
}
