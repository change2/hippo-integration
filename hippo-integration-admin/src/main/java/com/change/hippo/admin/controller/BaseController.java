package com.change.hippo.admin.controller;

import com.change.hippo.admin.entity.UserEntity;
import com.change.hippo.admin.utils.ShiroUtils;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
	public UserEntity getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}