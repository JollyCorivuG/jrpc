package bupt.edu.jhc.jrpc.server.tcp;

import bupt.edu.jhc.jrpc.domain.dto.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.dto.resp.RPCResp;
import bupt.edu.jhc.jrpc.protocol.ProtocolMsg;
import bupt.edu.jhc.jrpc.protocol.ProtocolMsgDecoder;
import bupt.edu.jhc.jrpc.protocol.ProtocolMsgEncoder;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgTypeEnum;
import bupt.edu.jhc.jrpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;

/**
 * @Description: TCP 请求处理器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
public class TcpServerHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket netSocket) {
        // 处理连接
        var bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 接受请求，解码
            ProtocolMsg<RPCReq> protocolMsg;
            try {
                protocolMsg = (ProtocolMsg<RPCReq>) ProtocolMsgDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误");
            }
            var rpcReq = protocolMsg.getBody();
            System.out.println("收到请求：" + rpcReq.getServiceName() + "." + rpcReq.getMethodName());

            // 处理请求
            // 构造响应结果对象
            RPCResp rpcResp = new RPCResp();
            try {
                // 获取要调用的服务实现类，通过反射调用
                var implClass = LocalRegistry.get(rpcReq.getServiceName());
                var method = implClass.getMethod(rpcReq.getMethodName(), rpcReq.getParameterTypes());
                var result = method.invoke(implClass.getDeclaredConstructor().newInstance(), rpcReq.getParams());
                // 封装返回结果
                rpcResp.setData(result);
                rpcResp.setDataType(method.getReturnType());
                rpcResp.setMsg("ok");
            } catch (Exception e) {
                System.out.println("RPC 服务调用失败：" + e.getMessage());
                rpcResp.setMsg(e.getMessage());
                rpcResp.setException(e);
            }

            // 发送响应，编码
            var header = protocolMsg.getHeader();
            header.setType(ProtocolMsgTypeEnum.RESPONSE.getType().byteValue());
            var respProtocolMsg = ProtocolMsg.builder()
                    .header(header)
                    .body(rpcResp)
                    .build();
            try {
                Buffer encode = ProtocolMsgEncoder.encode(respProtocolMsg);
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }
            System.out.println("响应已发送：" + rpcResp);
        });
        netSocket.handler(bufferHandlerWrapper);
    }
}
