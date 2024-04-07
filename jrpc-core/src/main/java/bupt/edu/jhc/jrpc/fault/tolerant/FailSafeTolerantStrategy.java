package bupt.edu.jhc.jrpc.fault.tolerant;

import bupt.edu.jhc.jrpc.domain.dto.resp.RPCResp;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 静默处理策略
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {

    @Override
    public RPCResp doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常", e);
        return new RPCResp();
    }
}
