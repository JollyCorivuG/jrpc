package bupt.edu.jhc.jrpc.fault.retry;

import bupt.edu.jhc.jrpc.domain.dto.resp.RPCResp;

import java.util.concurrent.Callable;

/**
 * @Description: 不重试策略
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/6
 */
public class NoRetryStrategy implements RetryStrategy {
    @Override
    public RPCResp doRetry(Callable<RPCResp> task) throws Exception {
        return task.call();
    }
}
