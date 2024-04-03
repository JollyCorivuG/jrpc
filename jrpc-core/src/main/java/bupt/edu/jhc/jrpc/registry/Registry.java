package bupt.edu.jhc.jrpc.registry;

import bupt.edu.jhc.jrpc.config.RegistryConfig;
import bupt.edu.jhc.jrpc.domain.ServiceMetaInfo;

import java.util.List;

/**
 * @Description: 注册中心接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/1
 */
public interface Registry {
    /**
     * 初始化
     *
     * @param config
     */
    void init(RegistryConfig config);

    /**
     * 心跳检测
     *
     */
    void heartBeat();

    /**
     * 注册服务
     *
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 取消注册
     *
     * @param serviceMetaInfo
     */
    void unregister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现
     *
     * @param serviceKey 服务键名
     * @return List<ServiceMetaInfo> 服务元消息列表
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 监听（消费端）
     *
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);

    /**
     * 服务销毁
     */
    void destroy();
}
