package bupt.edu.jhc.jrpc.domain.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: RPC 响应类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RPCResp implements Serializable {
    private Class<?> dataType; // 返回类型
    private Object data; // 响应数据
    private String msg; // 响应消息
    private Exception exception; // 异常信息
}
