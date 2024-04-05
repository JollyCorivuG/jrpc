package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 轮询负载均衡器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {
    @Override
    protected Selector createSector(List<ServiceMetaInfo> serviceMetaInfoList) {
        return new RoundRobinSelector(new AtomicInteger(0), serviceMetaInfoList);
    }

    @AllArgsConstructor
    private static class RoundRobinSelector implements Selector {
        private AtomicInteger currentIndex; // 当前节点下标
        private List<ServiceMetaInfo> serviceMetaInfoList; // 服务节点列表

        @Override
        public ServiceMetaInfo nxt(Map<String, Object> reqParams) {
            return serviceMetaInfoList.get(currentIndex.getAndIncrement() % serviceMetaInfoList.size());
        }
    }
}
