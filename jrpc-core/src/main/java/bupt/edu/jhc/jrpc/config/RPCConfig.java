package bupt.edu.jhc.jrpc.config;

import bupt.edu.jhc.jrpc.loadbalancer.LoadBalancerKeys;
import bupt.edu.jhc.jrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @Description: RPC 框架配置类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/15
 */
@Data
public class RPCConfig {
    private String name = "jRPC";
    private String version = "1.0.0";
    private String serverHost = "localhost";
    private Integer serverPort = 8090;
    private Boolean mock = false;
    private String serializer = SerializerKeys.JDK;
    private RegistryConfig registryConfig = new RegistryConfig();
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
}
