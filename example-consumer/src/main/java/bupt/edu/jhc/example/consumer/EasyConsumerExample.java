package bupt.edu.jhc.example.consumer;

import bupt.edu.jhc.example.common.model.User;
import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.config.RPCConfig;
import bupt.edu.jhc.jrpc.domain.constants.RPCConstants;
import bupt.edu.jhc.jrpc.proxy.ServiceProxyFactory;
import bupt.edu.jhc.jrpc.utils.ConfigUtils;

/**
 * @Description: 简易服务消费者示例
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        var rpcConfig = ConfigUtils.loadConfig(RPCConfig.class, RPCConstants.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpcConfig);

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        var user = new User();
        user.setName("JollyCorivuG");

        // 调用
        var newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("获取用户失败");
        }
    }

}
