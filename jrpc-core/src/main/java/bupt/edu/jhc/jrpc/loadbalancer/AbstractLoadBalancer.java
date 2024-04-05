package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 抽象负载均衡类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    private final Map<String, Selector> cache = new ConcurrentHashMap<>(); // 维护一个服务键与选择器的映射

    /**
     * 创建选择器, 由子类实现
     *
     * @param serviceMetaInfoList
     * @return
     */
    protected abstract Selector createSector(List<ServiceMetaInfo> serviceMetaInfoList);

    @Override
    public ServiceMetaInfo select(String serviceKey, Map<String, Object> reqParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        return Optional.ofNullable(cache.get(serviceKey)).orElseGet(() -> {
            Selector selector = createSector(serviceMetaInfoList);
            cache.put(serviceKey, selector);
            return selector;
        }).nxt(reqParams);
    }

    @Override
    public void reload(String serviceKey) {
        cache.remove(serviceKey);
    }
}
