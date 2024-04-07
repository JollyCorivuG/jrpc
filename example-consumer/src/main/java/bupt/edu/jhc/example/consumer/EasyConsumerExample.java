package bupt.edu.jhc.example.consumer;

import bupt.edu.jhc.example.common.model.User;
import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.bootstrap.ConsumerBootstrap;
import bupt.edu.jhc.jrpc.proxy.ServiceProxyFactory;

import java.util.stream.IntStream;

/**
 * @Description: 简易服务消费者示例
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        // 初始化
        ConsumerBootstrap.init();

        var userService = ServiceProxyFactory.getProxy(UserService.class);
        IntStream.range(0, 3).forEach(i -> {
            var user = new User();
            user.setName("JollyCorivuG" + i);
            var newUser = userService.getUser(user);
            if (newUser != null) {
                System.out.println(newUser.getName());
            } else {
                System.out.println("获取用户失败");
            }
        });
    }

}
