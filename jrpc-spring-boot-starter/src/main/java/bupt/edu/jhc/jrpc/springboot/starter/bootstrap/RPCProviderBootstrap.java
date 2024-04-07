package bupt.edu.jhc.jrpc.springboot.starter.bootstrap;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import bupt.edu.jhc.jrpc.registry.LocalRegistry;
import bupt.edu.jhc.jrpc.registry.Registry;
import bupt.edu.jhc.jrpc.registry.RegistryFactory;
import bupt.edu.jhc.jrpc.springboot.starter.annotation.RPCService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Description: RPC 服务提供者启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public class RPCProviderBootstrap implements BeanPostProcessor {
    /**
     * Bean 初始化后执行，注册服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        var rpcService = beanClass.getAnnotation(RPCService.class);
        if (rpcService != null) {
            // 1.获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            // 2.获得 RPC 全局配置
            var rpcConfig = RPCApplication.getRpcConfig();

            // 3.注册服务
            // 3.1本地注册
            LocalRegistry.register(serviceName, beanClass);
            // 3.2注册服务到注册中心
            var registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            var serviceMetaInfo = ServiceMetaInfo.builder()
                    .name(serviceName)
                    .version(serviceVersion)
                    .host(rpcConfig.getServerHost())
                    .port(rpcConfig.getServerPort())
                    .build();
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
