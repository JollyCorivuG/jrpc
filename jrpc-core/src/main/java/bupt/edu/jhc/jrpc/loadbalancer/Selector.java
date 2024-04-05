package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;

import java.util.Map;

/**
 * @Description: 负载均衡选择器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public interface Selector {
    ServiceMetaInfo nxt(Map<String, Object> reqParams);
}
