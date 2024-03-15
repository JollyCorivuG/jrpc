package bupt.edu.jhc.jrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @Description: 配置工具类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/15
 */
public class ConfigUtils {
    /**
     * 加载配置
     *
     * @param configClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> configClass, String prefix) {
        return loadConfig(configClass, prefix, "");
    }

    /**
     * 加载配置类
     *
     * @param configClass
     * @param prefix
     * @param env
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> configClass, String prefix, String env) {
        var file = new StringBuilder("application");
        if (StrUtil.isNotBlank(env)) {
            file.append("-").append(env);
        }
        file.append(".properties");
        var props = new Props(file.toString());
        return props.toBean(configClass, prefix);
    }
}
