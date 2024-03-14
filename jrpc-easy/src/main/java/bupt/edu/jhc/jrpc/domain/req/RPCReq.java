package bupt.edu.jhc.jrpc.domain.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: RPC 请求
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RPCReq implements Serializable {
    private String serviceName; // 服务名
    private String methodName; // 方法名
    private Class<?>[] parameterTypes; // 参数类型列表
    private Object[] params; // 参数列表
}
