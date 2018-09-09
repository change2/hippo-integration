package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.MenuEntity;
import com.change.hippo.admin.entity.Tree;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface MenuService {

	Tree<MenuEntity> getSysMenuTree(Long id);

	List<Tree<MenuEntity>> listMenuTree(Long id);

	Tree<MenuEntity> getTree();

	Tree<MenuEntity> getTree(Long id);

	List<MenuEntity> list(Map<String, Object> params);

	int remove(Long id);

	int save(MenuEntity menu);

	int update(MenuEntity menu);

	MenuEntity get(Long id);

	Set<String> listPerms(Long userId);

}
