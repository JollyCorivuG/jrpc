package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * @Description: 随机负载均衡器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {
    // 随机数生成器
    private static final RandomGeneratorFactory<RandomGenerator> l128X256MixRandom;
    private static final RandomGenerator randomGenerator;
    static {
        l128X256MixRandom = RandomGeneratorFactory.of("L128X256MixRandom");
        randomGenerator = l128X256MixRandom.create(System.currentTimeMillis());
    }

    @Override
    protected Selector createSector(List<ServiceMetaInfo> serviceMetaInfoList) {
        return new RandomSelector(serviceMetaInfoList);
    }

    @AllArgsConstructor
    private static class RandomSelector implements Selector {
        private List<ServiceMetaInfo> serviceMetaInfoList;

        @Override
        public ServiceMetaInfo nxt(Map<String, Object> reqParams) {
            return serviceMetaInfoList.get(randomGenerator.nextInt(serviceMetaInfoList.size()));
        }
    }
}
