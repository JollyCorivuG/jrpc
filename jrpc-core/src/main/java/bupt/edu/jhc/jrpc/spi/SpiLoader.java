package bupt.edu.jhc.jrpc.spi;

import bupt.edu.jhc.jrpc.serializer.Serializer;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Spi 加载器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/24
 */
@Slf4j
public class SpiLoader {
    /**
     * SPI 加载的信息
     */
    private static final Map<String, Map<String, Class<?>>> LOADER_MAP = new ConcurrentHashMap<>();
    /**
     * 实例缓存
     */
    private static final Map<String, Object> INSTANCE_CACHE = new ConcurrentHashMap<>();
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";
    private static final String[] SCAN_DIRS = {RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 需要加载的接口
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = List.of(Serializer.class);

    /**
     * 获取某个接口的实例
     *
     * @param clz
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<T> clz, String key) {
        var keyClassMap = LOADER_MAP.get(clz.getName());
        if (keyClassMap == null) {
            log.error("SpiLoader load spi class failed: {}", clz.getName());
            return null;
        }
        var clazz = keyClassMap.get(key);
        if (clazz == null) {
            log.error("SpiLoader load spi class failed: {}", key);
            return null;
        }
        var instance = INSTANCE_CACHE.get(clazz.getName());
        if (instance == null) {
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
                INSTANCE_CACHE.put(clazz.getName(), instance);
            } catch (Exception e) {
                log.error("SpiLoader load spi class failed: {}", e.getMessage());
            }
        }
        return clz.cast(instance);
    }

    /**
     * 加载所有 SPI 接口实现类
     */
    public static void loadAll() {
        log.info("SpiLoader start to load all spi classes~");
        LOAD_CLASS_LIST.forEach(SpiLoader::load);
    }

    /**
     * 加载某个 SPI 接口实现类
     *
     * @param clz
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> clz) {
        log.info("SpiLoader start to load spi class: {}", clz.getName());
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            var resources = ResourceUtil.getResources(scanDir + clz.getName());
            // 读取每个资源文件
            resources.forEach(resource -> {
                // 一行一行地读
                // hessian=bupt.edu.jhc.jrpc.HessianSerializer
                try (var reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // 分割键值对
                        var kv = line.split("=");
                        if (kv.length != 2) {
                            log.error("Invalid spi config: {}", line);
                            continue;
                        }
                        // 加载类
                        try {
                            var key = kv[0];
                            var className = kv[1];
                            var clazz = Class.forName(className);
                            keyClassMap.put(key, clazz);
                            log.info("SpiLoader load spi class: {} with key: {}", className, key);
                        } catch (ClassNotFoundException e) {
                            log.error("SpiLoader load spi class failed: {}", e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    log.error("SpiLoader load spi class failed: {}", e.getMessage());
                }
            });
        }
        LOADER_MAP.put(clz.getName(), keyClassMap);
        return keyClassMap;
    }

}

