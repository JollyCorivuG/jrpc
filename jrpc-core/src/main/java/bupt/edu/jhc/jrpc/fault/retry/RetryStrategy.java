package bupt.edu.jhc.jrpc.fault.retry;

import bupt.edu.jhc.jrpc.domain.dto.resp.RPCResp;

import java.util.concurrent.Callable;

/**
 * @Description: 重试策略接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/6
 */
public interface RetryStrategy {

    /**
     * 执行重试
     *
     * @param task 重试任务
     * @return RPCResp
     * @throws Exception
     */
    RPCResp doRetry(Callable<RPCResp> task) throws Exception;
}
