package bupt.edu.jhc.jrpc.bootstrap;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceRegisterInfo;
import bupt.edu.jhc.jrpc.registry.LocalRegistry;
import bupt.edu.jhc.jrpc.registry.Registry;
import bupt.edu.jhc.jrpc.registry.RegistryFactory;
import bupt.edu.jhc.jrpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * @Description: 服务提供者启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public class ProviderBootstrap {
    /**
     * 初始化
     *
     * @param serviceRegisterInfoList 服务注册信息列表
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) throws RuntimeException {
        // 1.RPC 框架初始化 (加载配置和注册中心)
        RPCApplication.init();

        // 2.获得 RPC 全局配置
        var rpcConfig = RPCApplication.getRpcConfig();

        // 3.注册服务
        for (var serviceRegisterInfo : serviceRegisterInfoList) {
            // 获取服务名
            var serviceName = serviceRegisterInfo.getName();
            // 3.1本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());
            // 3.2注册服务到注册中心
            var registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            var serviceMetaInfo = ServiceMetaInfo.builder()
                    .name(serviceName)
                    .host(rpcConfig.getServerHost())
                    .port(rpcConfig.getServerPort())
                    .build();
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        // 4.启动服务器
        var vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.start(rpcConfig.getServerPort());
    }
}
