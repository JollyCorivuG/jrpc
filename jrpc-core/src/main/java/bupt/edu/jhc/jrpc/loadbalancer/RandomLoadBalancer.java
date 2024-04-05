package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * @Description: 随机负载均衡器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public class RandomLoadBalancer implements LoadBalancer {
    private static final RandomGeneratorFactory<RandomGenerator> l128X256MixRandom;
    private static final RandomGenerator randomGenerator;
    static {
        l128X256MixRandom = RandomGeneratorFactory.of("L128X256MixRandom");
        randomGenerator = l128X256MixRandom.create(System.currentTimeMillis());
    }

    @Override
    public ServiceMetaInfo select(Map<String, Object> reqParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        return serviceMetaInfoList.get(randomGenerator.nextInt(serviceMetaInfoList.size()));
    }
}
