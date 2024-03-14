package bupt.edu.jhc.jrpc.registry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 本地注册表
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class LocalRegistry {
    private static final ConcurrentHashMap<String, Class<?>> SERVICE_HOLDER = new ConcurrentHashMap<>(); // 服务注册信息

    /**
     * 注册服务
     *
     * @param serviceName
     * @param implClass
     */
    public static void register(String serviceName, Class<?> implClass) {
        SERVICE_HOLDER.put(serviceName, implClass);
    }

    /**
     * 获取服务实现类
     *
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return SERVICE_HOLDER.get(serviceName);
    }

    /**
     * 移除服务
     *
     * @param serviceName
     */
    public static void remove(String serviceName) {
        SERVICE_HOLDER.remove(serviceName);
    }
}
