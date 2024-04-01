package bupt.edu.jhc.jrpc.config;

import bupt.edu.jhc.jrpc.registry.RegistryKeys;
import lombok.Data;

/**
 * @Description: RPC 框架注册中心配置
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/1
 */
@Data
public class RegistryConfig {
    private String registry = RegistryKeys.ETCD; // 注册中心类型
    private String address = "http://localhost:2380"; // 注册中心地址
    private String username; // 用户名
    private String password; // 密码
    private Long timeout = 10000L; // 超时时间(毫秒)
}
