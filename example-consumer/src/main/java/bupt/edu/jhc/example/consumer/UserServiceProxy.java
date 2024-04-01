package bupt.edu.jhc.example.consumer;

import bupt.edu.jhc.example.common.model.User;
import bupt.edu.jhc.example.common.service.UserService;
import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.resp.RPCResp;
import bupt.edu.jhc.jrpc.serializer.JDKSerializer;
import bupt.edu.jhc.jrpc.serializer.Serializer;
import cn.hutool.http.HttpRequest;

import java.io.IOException;

/**
 * @Description: 用户服务代理类 (静态代理)
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JDKSerializer();

        RPCReq rpcReq = RPCReq.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class<?>[]{User.class})
                .params(new Object[]{user})
                .build();
        try {
            var reqBytes = serializer.serialize(rpcReq);
            byte[] result;
            try (var resp = HttpRequest.post("http://localhost:8090")
                    .body(reqBytes)
                    .execute()) {
                result = resp.bodyBytes();
            }
            var rpcResp = serializer.deserialize(result, RPCResp.class);
            return (User) rpcResp.getData();
        } catch (IOException e) {
            System.out.println();
        }

        return null;
    }
}
