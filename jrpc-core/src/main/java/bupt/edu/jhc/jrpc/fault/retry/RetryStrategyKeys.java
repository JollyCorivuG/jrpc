package bupt.edu.jhc.jrpc.fault.retry;

/**
 * @Description: 重试策略键名常量
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/6
 */
public interface RetryStrategyKeys {
    String NO = "no"; // 不重试
    String FIXED_INTERVAL = "fixedInterval"; // 固定时间间隔
}
