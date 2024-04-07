package bupt.edu.jhc.jrpc.springboot.starter.bootstrap;

import bupt.edu.jhc.jrpc.proxy.ServiceProxyFactory;
import bupt.edu.jhc.jrpc.springboot.starter.annotation.RPCReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Description: RPC 服务消费者启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public class RPCConsumerBootstrap implements BeanPostProcessor {
    /**
     * Bean 初始化后执行，注入服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var beanClass = bean.getClass();
        // 遍历对象的所有属性
        var declaredFields = beanClass.getDeclaredFields();
        for (var field : declaredFields) {
            var rpcReference = field.getAnnotation(RPCReference.class);
            if (rpcReference != null) {
                // 为属性生成代理对象
                var interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                var proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    field.set(bean, proxyObject);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象失败", e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
