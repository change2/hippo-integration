# hippo integration
[![Total lines](https://tokei.rs/b1/github/changealice/hippo-integration?category=lines)](https://github.com/changealice/hippo-integration.git)


hippo 是个基于spring message 框架写的一个开源的api网关，他能够帮助你做到如下事情
- 服务路由转换内部接口成http服务，目前支持：http，dubbo,soap,hessian,rmi等协议
- 服务增加：快速给内部接口增加接入控制，数字签名，加密，限流（分布式限流、本地限流），熔断，服务降级，登录状态校验，参数校验
- 图形化配置，动态配置接口参数不重启（支持apollo配置）


# hippo未来支持包括
 
-  useragent,header的安全过滤 
-  接口聚合服务
-  基于json web token实现token验证
-  支持spring cloud服务转发
-  接口编排（DSL实现）
-  支持grpc服务转发



# hippo common功能列表
- 基于Retrofit、feign实现httpclient请求
- 基于redis cluster实现分布式锁，防重复
- 简化kafka消费，提供手动、自动提交offset
- 简化配置redis,其中包括redis单节点、redis cluster
- 订单号生成【基于数据库】
- 简化配置spring mvc，支持全局拦截错误，错误信息可配置，可动态加载
- 分布式链路跟踪，基于log4j2的MDC实现，目前支持http、dubbo、kafka，多线程

# hippo integration admin 功能列表
- 动态填写接口配置到hippo integration server中
- 菜单权限管理

