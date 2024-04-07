package bupt.edu.jhc.example.springboot.provider;

import bupt.edu.jhc.jrpc.springboot.starter.annotation.EnableRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@SpringBootApplication
@EnableRPC
public class ExampleSpringbootProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootProviderApplication.class, args);
    }
}
