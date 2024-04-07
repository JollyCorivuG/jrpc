package bupt.edu.jhc.jrpc.fault.tolerant;

import bupt.edu.jhc.jrpc.domain.dto.resp.RPCResp;

import java.util.Map;

/**
 * @Description: 容错策略接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public interface TolerantStrategy {
    /**
     * 容错
     *
     * @param context 上下文，用于传递数据
     * @param e       异常
     * @return
     */
    RPCResp doTolerant(Map<String, Object> context, Exception e);
}
