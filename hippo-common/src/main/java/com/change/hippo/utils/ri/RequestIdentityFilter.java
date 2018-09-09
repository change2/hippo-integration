package com.change.hippo.utils.ri;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 请求身份生成过滤器
 *
 */
public class RequestIdentityFilter implements Filter {

	private final static Logger LOGGER = LoggerFactory.getLogger(RequestIdentityFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("RequestIdentity过滤器启动");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			String rid, name, version, step;
			HttpServletRequest req = ((HttpServletRequest)request);
			rid = req.getHeader("rid"); // 上层调用者传递请求唯一标志,优先从header中取
			rid = StringUtils.isEmpty(rid)? request.getParameter("_rid") : rid; // 其次从参数中查找
			if(StringUtils.isEmpty(rid)){
				rid = RequestIdentityHolder.generateRid(); // 如果调用方未传递,则自动生成
				name = RequestIdentityHolder.getName();
				version = RequestIdentityHolder.getVersion();
				step = "0";
			}else {
				step = req.getHeader("rstep");
				step = StringUtils.isEmpty(step)? request.getParameter("_rstep") : step;
				step = StringUtils.isEmpty(step)? "1" : step; // 如果存在RID,则至少是第二跳
				name = req.getHeader("rname");
				name = StringUtils.isEmpty(name)? request.getParameter("_rname") : name;
				if(StringUtils.isNotEmpty(name)){
					name = URLDecoder.decode(name, "UTF-8");
				}
				version = req.getHeader("rversion");
				version = StringUtils.isEmpty(version)? request.getParameter("_rversion") : version;
			}
			RequestInfo requestInfo = RequestIdentityHolder.get(true); // 该方式能够在高并发时减少大量对象的创建与回收操作,提高效率
			requestInfo.setId(rid);
			requestInfo.setIp(RequestUtils.getRemoteIP(req));
			requestInfo.setStep(Integer.valueOf(step) + 1);
			requestInfo.setName(name);
			requestInfo.setVersion(version);
			RequestIdentityHolder.set(requestInfo);
		} catch (Exception e) {
			LOGGER.warn("拦截RequestIdentity信息时出现错误", e);
		}
        chain.doFilter(request, response);
    }

	@Override
	public void destroy() {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("RequestIdentity过滤器销毁");
		}
	}

}

