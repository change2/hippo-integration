package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

	RoleEntity get(Long id);

	List<RoleEntity> list();

	int save(RoleEntity role);

	int update(RoleEntity role);

	int remove(Long id);

	List<RoleEntity> list(Long userId);

	int batchremove(Long[] ids);
}
