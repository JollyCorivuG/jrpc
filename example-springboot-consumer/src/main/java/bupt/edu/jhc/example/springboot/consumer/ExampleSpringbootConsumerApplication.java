package bupt.edu.jhc.example.springboot.consumer;

import bupt.edu.jhc.jrpc.springboot.starter.annotation.EnableRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@SpringBootApplication
@EnableRPC(needServer = false)
public class ExampleSpringbootConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootConsumerApplication.class, args);
    }
}
