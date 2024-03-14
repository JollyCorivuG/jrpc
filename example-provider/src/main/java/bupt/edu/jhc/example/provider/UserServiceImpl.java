package bupt.edu.jhc.example.provider;

import bupt.edu.jhc.example.common.model.User;
import bupt.edu.jhc.example.common.service.UserService;

/**
 * @Description: 用户服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
