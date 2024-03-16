package bupt.edu.jhc.jrpc.proxy;

import bupt.edu.jhc.jrpc.RPCApplication;

import java.lang.reflect.Proxy;

/**
 * @Description: 服务代理工厂
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     *
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RPCApplication.getRpcConfig().getMock()) {
            return getMockProxy(serviceClass);
        }
        return serviceClass.cast(Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()));
    }

    /**
     * 获取 mock 代理对象
     *
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return serviceClass.cast(Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy()));
    }
}
