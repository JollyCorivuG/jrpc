package bupt.edu.jhc.jrpc.springboot.starter.bootstrap;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.server.tcp.VertxTcpServer;
import bupt.edu.jhc.jrpc.springboot.starter.annotation.EnableRPC;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

/**
 * @Description: RPC 框架全局启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public class RPCInitBootstrap implements ImportBeanDefinitionRegistrar {
    /**
     * Spring 初始化时执行，初始化 RPC 框架
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        var needServer = (boolean) Objects.requireNonNull(importingClassMetadata.getAnnotationAttributes(EnableRPC.class.getName())).get("needServer");

        // RPC 框架初始化 (加载配置和注册中心)
        RPCApplication.init();

        // 全局配置
        var rpcConfig = RPCApplication.getRpcConfig();

        // 启动服务器
        if (needServer) {
            var vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.start(rpcConfig.getServerPort());
        } else {
            System.out.println("RPC 服务器未启动");
        }

    }
}
