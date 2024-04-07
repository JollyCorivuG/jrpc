package bupt.edu.jhc.jrpc.springboot.starter.annotation;

import bupt.edu.jhc.jrpc.springboot.starter.bootstrap.RPCConsumerBootstrap;
import bupt.edu.jhc.jrpc.springboot.starter.bootstrap.RPCInitBootstrap;
import bupt.edu.jhc.jrpc.springboot.starter.bootstrap.RPCProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 是否启用 RPC 注解
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RPCInitBootstrap.class, RPCProviderBootstrap.class, RPCConsumerBootstrap.class})
public @interface EnableRPC {
    /**
     * 是否需要启动 web 服务器
     *
     * @return
     */
    boolean needServer() default true;
}
