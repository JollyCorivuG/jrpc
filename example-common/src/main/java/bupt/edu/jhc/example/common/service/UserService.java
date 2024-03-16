package bupt.edu.jhc.example.common.service;

import bupt.edu.jhc.example.common.model.User;

/**
 * @Description: 用户服务
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public interface UserService {
    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    default short getNumber() {
        return 1;
    }
}
