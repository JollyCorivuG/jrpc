package bupt.edu.jhc.example.springboot.provider;

import bupt.edu.jhc.example.common.model.User;
import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.springboot.starter.annotation.RPCService;
import org.springframework.stereotype.Service;

/**
 * @Description: 用户服务实现
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/7
 */
@Service
@RPCService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
