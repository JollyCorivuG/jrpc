package bupt.edu.jhc.jrpc.registry;

import bupt.edu.jhc.jrpc.config.RegistryConfig;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Description: 注册中心测试
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/1
 */
public class RegistryTest {
    final Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setName("myService");
        serviceMetaInfo.setVersion("1.0");
        serviceMetaInfo.setHost("localhost");
        serviceMetaInfo.setPort(1234);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setName("myService");
        serviceMetaInfo.setVersion("1.0");
        serviceMetaInfo.setHost("localhost");
        serviceMetaInfo.setPort(1235);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setName("myService");
        serviceMetaInfo.setVersion("2.0");
        serviceMetaInfo.setHost("localhost");
        serviceMetaInfo.setPort(1234);
        registry.register(serviceMetaInfo);
    }

    @Test
    public void unRegister() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setName("myService");
        serviceMetaInfo.setVersion("1.0");
        serviceMetaInfo.setHost("localhost");
        serviceMetaInfo.setPort(1234);
        registry.unregister(serviceMetaInfo);
    }

    @Test
    public void serviceDiscovery() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setName("myService");
        serviceMetaInfo.setVersion("1.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        Assert.assertNotNull(serviceMetaInfoList);
    }

    @Test
    public void heartBeat() throws Exception {
        // init 方法中已经执行心跳检测了
        register();
        // 阻塞 1 分钟
        Thread.sleep(60 * 1000L);
    }
}
