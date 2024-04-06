package bupt.edu.jhc.jrpc.fault.retry;

import bupt.edu.jhc.jrpc.domain.dto.resp.RPCResp;
import com.github.rholder.retry.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 固定间隔重试策略
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/6
 */
public class FixedIntervalRetryStrategy implements RetryStrategy {

    @Override
    public RPCResp doRetry(Callable<RPCResp> task) throws Exception {
        return RetryerBuilder.<RPCResp>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("重试次数: " + attempt.getAttemptNumber());
                    }
                })
                .build()
                .call(task);
    }
}
