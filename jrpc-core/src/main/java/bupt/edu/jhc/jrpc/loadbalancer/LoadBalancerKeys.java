package bupt.edu.jhc.jrpc.loadbalancer;

/**
 * @Description: 负载均衡键名常量
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public interface LoadBalancerKeys {
    String ROUND_ROBIN = "roundRobin"; // 轮询

    String RANDOM = "random"; // 随机

    String CONSISTENT_HASH = "consistentHash"; // 一致性哈希
}
