package bupt.edu.jhc.jrpc.proxy;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.ServiceMetaInfo;
import bupt.edu.jhc.jrpc.domain.constants.RPCConstants;
import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.registry.Registry;
import bupt.edu.jhc.jrpc.registry.RegistryFactory;
import bupt.edu.jhc.jrpc.server.tcp.VertxTcpClient;
import cn.hutool.core.collection.CollUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description: 服务代理 (动态代理)
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建 RPC 请求
        RPCReq rpcReq = RPCReq.builder()
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

        // 选择第一个服务提供者 (后期可优化为负载均衡)
        var selectedService = serviceMetaInfoList.getFirst();

        // 发送 TCP 请求
        return VertxTcpClient.req(rpcReq, selectedService).getData();
    }
}
