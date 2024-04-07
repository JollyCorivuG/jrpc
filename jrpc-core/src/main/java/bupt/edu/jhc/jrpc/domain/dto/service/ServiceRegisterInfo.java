package bupt.edu.jhc.jrpc.domain.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 服务注册信息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRegisterInfo<T> {
    private String name; // 服务名称
    private Class<? extends T> implClass; // 服务实现类
}
