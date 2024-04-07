package bupt.edu.jhc.example.provider;


import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.bootstrap.ProviderBootstrap;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceRegisterInfo;

import java.util.ArrayList;

/**
 * @Description: 建议服务提供者示例
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class EasyProviderExample {
    public static void main(String[] args) throws RuntimeException {
        // 要注册的服务
        var serviceRegisterInfoList = new ArrayList<ServiceRegisterInfo<?>>();
        var serviceRegisterInfo = ServiceRegisterInfo.<UserService>builder()
                .name(UserService.class.getName())
                .implClass(UserServiceImpl.class)
                .build();
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
