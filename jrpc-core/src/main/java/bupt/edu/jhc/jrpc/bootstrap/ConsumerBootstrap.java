package bupt.edu.jhc.jrpc.bootstrap;

import bupt.edu.jhc.jrpc.RPCApplication;

/**
 * @Description: 服务消费者启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public class ConsumerBootstrap {
    /**
     * 初始化
     */
    public static void init() {
        // RPC 框架初始化 (配置和注册中心)
        RPCApplication.init();
    }
}
