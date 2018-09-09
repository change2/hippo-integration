package com.change.hippo.admin.service;

import com.change.hippo.admin.entity.Tree;
import com.change.hippo.admin.entity.UserEntity;
import com.change.hippo.admin.vo.UserVO;
import com.change.hippo.admin.entity.DeptEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface UserService {

    UserEntity get(Long id);

    List<UserEntity> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(UserEntity user);

    int update(UserEntity user);

    int remove(Long userId);

    int batchremove(Long[] userIds);

    boolean exit(Map<String, Object> params);

    Set<String> listRoles(Long userId);

    int resetPwd(UserVO userVO, UserEntity userDO) throws Exception;

    int adminResetPwd(UserVO userVO) throws Exception;

    Tree<DeptEntity> getTree();

    /**
     * 更新个人信息
     *
     * @param userDO
     * @return
     */
    int updatePersonal(UserEntity userDO);

}
