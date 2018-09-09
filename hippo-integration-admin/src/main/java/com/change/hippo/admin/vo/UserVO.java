package com.change.hippo.admin.vo;

import com.change.hippo.admin.entity.UserEntity;
import lombok.Data;

/**
 * @author gaoyuzhe
 * @date 2017/12/15.
 */
@Data
public class UserVO {

    /**
     * 更新的用户对象
     */
    private UserEntity userDO = new UserEntity();
    /**
     * 旧密码
     */
    private String pwdOld;
    /**
     * 新密码
     */
    private String pwdNew;

}
