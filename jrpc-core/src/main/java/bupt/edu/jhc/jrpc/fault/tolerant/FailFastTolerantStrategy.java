package bupt.edu.jhc.jrpc.fault.tolerant;

import bupt.edu.jhc.jrpc.domain.dto.resp.RPCResp;

import java.util.Map;

/**
 * @Description: 快速失败策略
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RPCResp doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}