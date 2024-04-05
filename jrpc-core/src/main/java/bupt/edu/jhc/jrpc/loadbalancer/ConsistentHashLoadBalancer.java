package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * @Description: 一致性哈希负载均衡器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public class ConsistentHashLoadBalancer extends AbstractLoadBalancer {
    private static final int VIRTUAL_NODE_NUM = 100; // 虚拟节点数

    @Override
    protected Selector createSector(List<ServiceMetaInfo> serviceMetaInfoList) {
        return new ConsistentHashSelector(serviceMetaInfoList);
    }

    private static class ConsistentHashSelector implements Selector {
        private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>(); // 一致性哈希环, 存放虚拟节点

        private int getHash(Object key) {
            return key.hashCode();
        }

        private ConsistentHashSelector(List<ServiceMetaInfo> serviceMetaInfoList) {
            // 构建一致性哈希环
            for (var serviceMetaInfo : serviceMetaInfoList) {
                IntStream.range(0, VIRTUAL_NODE_NUM).forEach(i -> {
                    var hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                    virtualNodes.put(hash, serviceMetaInfo);
                });
            }
        }

        @Override
        public ServiceMetaInfo nxt(Map<String, Object> reqParams) {
            // 获取请求参数的哈希值
            var hash = getHash(reqParams.toString());

            // 获取大于等于该哈希值的第一个节点
            var entry = virtualNodes.ceilingEntry(hash);
            return Objects.isNull(entry) ? virtualNodes.firstEntry().getValue() : entry.getValue();
        }
    }
}
