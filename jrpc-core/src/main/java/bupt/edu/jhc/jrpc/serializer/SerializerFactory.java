package bupt.edu.jhc.jrpc.serializer;

import bupt.edu.jhc.jrpc.spi.SpiLoader;

/**
 * @Description: 序列化器工厂
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/24
 */
public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 获取序列化器
     *
     * @param key 序列化器键名
     * @return 序列化器
     */
    public static Serializer getSerializer(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
