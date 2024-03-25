package bupt.edu.jhc.jrpc.server;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.resp.RPCResp;
import bupt.edu.jhc.jrpc.registry.LocalRegistry;
import bupt.edu.jhc.jrpc.serializer.Serializer;
import bupt.edu.jhc.jrpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

import java.io.IOException;

/**
 * @Description: HTTP 请求处理器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest req) {
        System.out.println("收到请求：" + req.uri() + "，请求方法：" + req.method());
        // 根据配置文件获取序列化器
        Serializer serializer = SerializerFactory.getSerializer(RPCApplication.getRpcConfig().getSerializer());

        // 异步处理 HTTP 请求
        req.bodyHandler(body -> {
            // 1.构建 RPC 请求
            var data = body.getBytes();
            RPCReq rpcReq = null;
            try {
                rpcReq = serializer.deserialize(data, RPCReq.class); // 反序列化
            } catch (IOException e) {
                System.out.println("RPC 请求反序列化失败：" + e.getMessage());
            }

            // 2.构建 RPC 响应
            var rpcResp = new RPCResp();
            if (rpcReq == null) {
                rpcResp.setMsg("RPC 请求为空");
                doResp(req, rpcResp, serializer);
                return;
            }
            try { // 基于反射机制调用服务实现类的方法
                var implClass = LocalRegistry.get(rpcReq.getServiceName());
                var method = implClass.getMethod(rpcReq.getMethodName(), rpcReq.getParameterTypes());
                var result = method.invoke(implClass.getDeclaredConstructor().newInstance(), rpcReq.getParams());
                // 封装 RPC 响应
                rpcResp.setData(result);
                rpcResp.setDataType(method.getReturnType());
                rpcResp.setMsg("success");
            } catch (Exception e) {
                System.out.println("RPC 服务调用失败：" + e.getMessage());
                rpcResp.setMsg("RPC 服务调用失败：" + e.getMessage());
                rpcResp.setException(e);
            }
            doResp(req, rpcResp, serializer);

        });
    }

    /**
     * 返回响应
     *
     * @param req
     * @param rpcResp
     * @param serializer
     */
    private void doResp(HttpServerRequest req, RPCResp rpcResp, Serializer serializer) {
        var resp = req.response().putHeader("content-type", "application/json");

        // 序列化 RPC 响应
        try {
            var data = serializer.serialize(rpcResp);
            resp.end(Buffer.buffer(data));
        } catch (IOException e) {
            System.out.println("RPC 响应序列化失败：" + e.getMessage());
            resp.end(Buffer.buffer());
        }
    }
}
