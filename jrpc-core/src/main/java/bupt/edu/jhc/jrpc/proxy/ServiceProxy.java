package bupt.edu.jhc.jrpc.proxy;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.constants.RPCConstants;
import bupt.edu.jhc.jrpc.domain.dto.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import bupt.edu.jhc.jrpc.fault.retry.RetryStrategy;
import bupt.edu.jhc.jrpc.fault.retry.RetryStrategyFactory;
import bupt.edu.jhc.jrpc.loadbalancer.LoadBalancer;
import bupt.edu.jhc.jrpc.loadbalancer.LoadBalancerFactory;
import bupt.edu.jhc.jrpc.registry.Registry;
import bupt.edu.jhc.jrpc.registry.RegistryFactory;
import bupt.edu.jhc.jrpc.server.tcp.VertxTcpClient;
import cn.hutool.core.collection.CollUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Description: 服务代理 (动态代理)
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建 RPC 请求
        var rpcReq = RPCReq.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(args)
                .build();
        // 从注册中心获取服务提供者请求地址
        var rpcConfig = RPCApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        var serviceMetaInfo = ServiceMetaInfo.builder()
                .name(method.getDeclaringClass().getName())
                .version(RPCConstants.DEFAULT_SERVICE_VERSION)
                .build();
        var serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // 将调用方法名（请求路径）作为负载均衡参数
        var requestParams = new HashMap<String, Object>();
        requestParams.put("methodName", rpcReq.getMethodName());
        var selectedService = loadBalancer.select(serviceMetaInfo.getServiceKey(), requestParams, serviceMetaInfoList);

        // 使用重试机制, 发送 TCP 请求
        RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
        var rpcResp = retryStrategy.doRetry(() -> VertxTcpClient.req(rpcReq, selectedService));
        return rpcResp.getData();
    }
}
