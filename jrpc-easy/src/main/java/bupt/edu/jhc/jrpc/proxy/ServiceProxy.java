package bupt.edu.jhc.jrpc.proxy;

import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.resp.RPCResp;
import bupt.edu.jhc.jrpc.serializer.JDKSerializer;
import bupt.edu.jhc.jrpc.serializer.Serializer;
import cn.hutool.http.HttpRequest;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description: 服务代理
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = new JDKSerializer();

        RPCReq rpcReq = RPCReq.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(args)
                .build();
        try {
            var reqBytes = serializer.serialize(rpcReq);
            byte[] result;
            try (var resp = HttpRequest.post("http://localhost:8080")
                    .body(reqBytes)
                    .execute()) {
                result = resp.bodyBytes();
            }
            var rpcResp = serializer.deserialize(result, RPCResp.class);
            return rpcResp.getData();
        } catch (IOException e) {
            System.out.println("RPC 请求失败：" + e.getMessage());
        }

        return null;
    }
}
