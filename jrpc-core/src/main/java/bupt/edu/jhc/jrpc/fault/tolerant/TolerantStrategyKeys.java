package bupt.edu.jhc.jrpc.fault.tolerant;

/**
 * @Description: 容错策略键名常量
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
public interface TolerantStrategyKeys {
    String FAIL_FAST = "failFast"; // 快速失败
    String FAIL_SAFE = "failSafe"; // 静默处理
}
