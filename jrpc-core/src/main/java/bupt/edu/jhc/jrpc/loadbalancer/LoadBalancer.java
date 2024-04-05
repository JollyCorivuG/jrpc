package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 负载均衡器接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public interface LoadBalancer {
    /**
     * 选择一个服务节点
     *
     * @param reqParams 请求参数, 用于进行一致性哈希
     * @param serviceMetaInfoList 服务节点列表
     * @return 选中的服务节点
     */
    ServiceMetaInfo select(String serviceKey, Map<String, Object> reqParams, List<ServiceMetaInfo> serviceMetaInfoList);

    /**
     * 重新加载服务节点
     *
     * @param serviceKey 服务键
     */
    void reload(String serviceKey);
}
