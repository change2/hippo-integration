package com.change.hippo.utils.spring.mvc.support;


import com.change.hippo.utils.result.ActionResult;
import com.change.hippo.utils.result.StatusCode;

/**
 * User: change.long
 * Date: 2017/8/21
 * Time: 下午4:54
 */
public class MsgUtil {
    public static Msg create(ActionResult result) {
        Msg msg = new Msg();
        msg.setCode(Integer.parseInt(result.getCode()));
        Object data = result.getResult();
        msg.setData(data);
        msg.setMsg(result.getMessage());
        msg.setOk(StatusCode.SUCCESS.getCode().equals(result.getCode()));
        return msg;
    }
}
