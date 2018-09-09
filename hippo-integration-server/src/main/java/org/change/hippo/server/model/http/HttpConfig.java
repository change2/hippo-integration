package org.change.hippo.server.model.http;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.change.hippo.server.model.ServiceConfig;

/**
 * User: change.long
 * Date: 2017/11/20
 * Time: 下午10:25
 * http请求配置
 */
@Getter
@Setter
@ToString
public class HttpConfig extends ServiceConfig {

    /**
     * 请求url
     */
    private String domain;

    /**
     * 请求的路径
     */
    private String path;

    /**
     * 默认为post
     */
    private String method = "POST";
}
