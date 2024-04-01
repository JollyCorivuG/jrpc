package bupt.edu.jhc.jrpc;

import bupt.edu.jhc.jrpc.config.RPCConfig;
import bupt.edu.jhc.jrpc.domain.constants.RPCConstants;
import bupt.edu.jhc.jrpc.registry.Registry;
import bupt.edu.jhc.jrpc.registry.RegistryFactory;
import bupt.edu.jhc.jrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: RPC 启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/15
 */
@Slf4j
public class RPCApplication {
    private static volatile RPCConfig rpcConfig;

    public static void init(RPCConfig newRPCConfig) {
        // RPC 配置初始化
        rpcConfig = newRPCConfig;
        log.info("RPC init, config = {}", rpcConfig.toString());

        // 注册中心初始化
        var registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("Registry init, config = {}", registryConfig.toString());
    }

    public static void init() {
        RPCConfig rpcConfig;
        try {
            rpcConfig = ConfigUtils.loadConfig(RPCConfig.class, RPCConstants.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            rpcConfig = new RPCConfig();
        }
        init(rpcConfig);
    }

    /**
     * 获取 RPC 配置, 采用双检索单例模式
     *
     * @return
     */
    public static RPCConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RPCApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
