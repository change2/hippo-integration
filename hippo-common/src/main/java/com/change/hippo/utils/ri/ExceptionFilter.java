package com.change.hippo.utils.ri;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.service.GenericService;

import com.change.hippo.utils.result.BaseException;
import com.change.hippo.utils.result.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Activate(group = Constants.PROVIDER, order = -1000)
public class ExceptionFilter implements Filter {

	public Logger logger;

	public ExceptionFilter() {
		this(LoggerFactory.getLogger(ExceptionFilter.class));
	}

	public ExceptionFilter(Logger logger) {
		this.logger = logger;
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		try {
			Result e = invoker.invoke(invocation);
			if (e.hasException() && GenericService.class != invoker.getInterface()) {
				try {
					Throwable exceptionInternal = e.getException();
					logger.error("Got unchecked and undeclared exception which called by "
							+ RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName()
							+ ", method: " + invocation.getMethodName() + ", exception: "
							+ exceptionInternal.getClass().getName() + ": " + exceptionInternal.getMessage(),
							exceptionInternal);

					exceptionInternal.printStackTrace();
					return (exceptionInternal instanceof BaseException ? e
							: (new RpcResult(new BaseException(StatusCode.SERVER_ERROR_CODE, exceptionInternal))));
				} catch (Throwable throwsCatch) {
					logger.error("Got unchecked and undeclared exception which called by "
							+ RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName()
							+ ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName()
							+ ": " + throwsCatch.getMessage(), e);

					throwsCatch.printStackTrace();
					return e;
				}
			} else {
				return e;
			}
		} catch (RuntimeException exception) {
			logger.error("Got unchecked and undeclared exception which called by "
					+ RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName()
					+ ", method: " + invocation.getMethodName() + ", exception: " + exception.getClass().getName()
					+ ": " + exception.getMessage(), exception);
			exception.printStackTrace();
			throw exception;
		}
	}
}
