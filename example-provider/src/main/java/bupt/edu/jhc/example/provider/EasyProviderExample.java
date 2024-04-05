package bupt.edu.jhc.example.provider;


import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import bupt.edu.jhc.jrpc.registry.LocalRegistry;
import bupt.edu.jhc.jrpc.registry.Registry;
import bupt.edu.jhc.jrpc.registry.RegistryFactory;
import bupt.edu.jhc.jrpc.server.Server;
import bupt.edu.jhc.jrpc.server.tcp.VertxTcpServer;

/**
 * @Description: 建议服务提供者示例
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // RPC 框架初始化
        RPCApplication.init();

        // 注册服务
        var serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        var rpcConfig = RPCApplication.getRpcConfig();
        var registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        var serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setName(serviceName);
        serviceMetaInfo.setVersion("1.0");
        serviceMetaInfo.setHost(rpcConfig.getServerHost());
        serviceMetaInfo.setPort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 TCP 服务
        Server tcpServer  = new VertxTcpServer();
        tcpServer.start(rpcConfig.getServerPort());
    }
}
