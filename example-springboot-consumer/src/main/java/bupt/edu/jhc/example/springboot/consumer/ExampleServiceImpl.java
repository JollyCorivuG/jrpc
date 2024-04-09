package bupt.edu.jhc.example.springboot.consumer;

import bupt.edu.jhc.example.common.model.User;
import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.springboot.starter.annotation.RPCReference;
import org.springframework.stereotype.Service;

/**
 * @Description: 示例服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@Service
public class ExampleServiceImpl {

    /**
     * 使用 RPC 框架注入
     */
    @RPCReference
    private UserService userService;

    /**
     * 测试方法
     */
    public void test() {
        var user = new User();
        user.setName("JollyCorivuG");
        var resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
