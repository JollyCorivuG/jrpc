package bupt.edu.jhc.jrpc.loadbalancer;

import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 负载均衡测试
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/5
 */
public class LoadBalancerTest {
    private final LoadBalancer loadBalancer = new RandomLoadBalancer();

    @Test
    public void select() {
        // 请求参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", "apple");

        // 服务列表
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setName("myService");
        serviceMetaInfo1.setVersion("1.0");
        serviceMetaInfo1.setHost("localhost");
        serviceMetaInfo1.setPort(1234);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setName("myService");
        serviceMetaInfo2.setVersion("1.0");
        serviceMetaInfo2.setHost("yupi.icu");
        serviceMetaInfo2.setPort(80);
        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2);
        // 连续调用 3 次
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(serviceMetaInfo1.getServiceKey(), requestParams, serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(serviceMetaInfo1.getServiceKey(), requestParams, serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(serviceMetaInfo1.getServiceKey(), requestParams, serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
    }
}
