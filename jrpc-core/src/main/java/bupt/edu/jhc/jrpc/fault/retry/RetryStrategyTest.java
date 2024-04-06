package bupt.edu.jhc.jrpc.fault.retry;

import org.junit.Test;

/**
 * @Description: 重试策略测试
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
        * @CreateTime: 2024/4/6
        */
public class RetryStrategyTest {
    private final RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            var rpcResp = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResp);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
