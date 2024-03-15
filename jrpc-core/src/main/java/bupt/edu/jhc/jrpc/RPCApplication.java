package bupt.edu.jhc.jrpc;

import bupt.edu.jhc.jrpc.config.RPCConfig;
import bupt.edu.jhc.jrpc.domain.constants.RPCConstants;
import bupt.edu.jhc.jrpc.utils.ConfigUtils;

/**
 * @Description: RPC 启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/15
 */
public class RPCApplication {
    private static volatile RPCConfig rpcConfig;

    public static void init(RPCConfig newRPCConfig) {
        rpcConfig = newRPCConfig;
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
