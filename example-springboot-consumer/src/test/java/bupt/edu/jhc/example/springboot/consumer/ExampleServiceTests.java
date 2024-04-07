package bupt.edu.jhc.example.springboot.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Description: 测试
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@SpringBootTest
public class ExampleServiceTests {
    @Resource
    private ExampleServiceImpl exampleServiceImpl;

    @Test
    public void test() {
        exampleServiceImpl.test();
    }
}
