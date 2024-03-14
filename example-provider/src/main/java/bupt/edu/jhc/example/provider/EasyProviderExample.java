package bupt.edu.jhc.example.provider;


import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.registry.LocalRegistry;
import bupt.edu.jhc.jrpc.server.VertxHttpServer;

/**
 * @Description: 建议服务提供者示例
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        var httpServer = new VertxHttpServer();
        httpServer.start(8080);
    }
}
