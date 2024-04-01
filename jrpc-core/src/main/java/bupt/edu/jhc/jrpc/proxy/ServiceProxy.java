package bupt.edu.jhc.jrpc.proxy;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.ServiceMetaInfo;
import bupt.edu.jhc.jrpc.domain.constants.RPCConstants;
import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.resp.RPCResp;
import bupt.edu.jhc.jrpc.registry.Registry;
import bupt.edu.jhc.jrpc.registry.RegistryFactory;
import bupt.edu.jhc.jrpc.serializer.Serializer;
import bupt.edu.jhc.jrpc.serializer.SerializerFactory;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;

import java.io.IOException;
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
        // 获取配置文件指定的序列化器
        Serializer serializer = SerializerFactory.getSerializer(RPCApplication.getRpcConfig().getSerializer());

        RPCReq rpcReq = RPCReq.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(args)
                .build();
        try {
            var reqBytes = serializer.serialize(rpcReq);

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

            // 发送请求
            byte[] result;
            try (var resp = HttpRequest.post(selectedService.getServiceAddress())
                    .body(reqBytes)
                    .execute()) {
                result = resp.bodyBytes();
            }
            var rpcResp = serializer.deserialize(result, RPCResp.class);
            return rpcResp.getData();
        } catch (IOException e) {
            System.out.println("RPC 请求失败：" + e.getMessage());
        }

        return null;
    }
}
