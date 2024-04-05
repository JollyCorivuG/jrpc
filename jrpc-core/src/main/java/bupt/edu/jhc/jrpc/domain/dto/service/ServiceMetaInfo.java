package bupt.edu.jhc.jrpc.domain.dto.service;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 服务元消息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMetaInfo {
    private String name; // 服务名
    private String version; // 版本
    private String host; // 主机名
    private Integer port; // 端口
    private String group; // 分组

    /**
     * 获取服务键名
     *
     * @return
     */
    public String getServiceKey() {
//        return String.format("%s:%s:%s", name, version, group);
        return String.format("%s:%s", name, version);
    }

    /**
     * 获取服务节点键名
     *
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), host, port);
    }

    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(host, "http")) {
            return String.format("http://%s:%s", host, port);
        }
        return String.format("%s:%s", host, port);
    }
}
