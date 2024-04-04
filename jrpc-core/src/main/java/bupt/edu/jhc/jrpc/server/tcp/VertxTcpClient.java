package bupt.edu.jhc.jrpc.server.tcp;

import bupt.edu.jhc.jrpc.RPCApplication;
import bupt.edu.jhc.jrpc.domain.ServiceMetaInfo;
import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.resp.RPCResp;
import bupt.edu.jhc.jrpc.protocol.ProtocolConstants;
import bupt.edu.jhc.jrpc.protocol.ProtocolMsg;
import bupt.edu.jhc.jrpc.protocol.ProtocolMsgDecoder;
import bupt.edu.jhc.jrpc.protocol.ProtocolMsgEncoder;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgSerializerEnum;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgTypeEnum;
import cn.hutool.core.util.IdUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Description: Vert.x TCP 客户端
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
public class VertxTcpClient {
    public static RPCResp req(RPCReq rpcReq, ServiceMetaInfo selectedService) throws InterruptedException, ExecutionException {
        var vertx = Vertx.vertx();
        var netClient = vertx.createNetClient();
        var responseFuture = new CompletableFuture<RPCResp>();
        netClient.connect(selectedService.getPort(), selectedService.getHost(),
                result -> {
                    if (result.succeeded()) {
                        System.out.println("Connected to TCP server");
                        var socket = result.result();
                        // 发送数据
                        // 构造消息
                        var protocolMessage = new ProtocolMsg<RPCReq>();
                        var header = new ProtocolMsg.Header();
                        header.setMagic(ProtocolConstants.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstants.PROTOCOL_VERSION);
                        header.setSerializer(ProtocolMsgSerializerEnum.of(RPCApplication.getRpcConfig().getSerializer()).getKey().byteValue());
                        header.setType(ProtocolMsgTypeEnum.REQUEST.getType().byteValue());
                        header.setRequestId(IdUtil.getSnowflakeNextId());
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcReq);
                        // 编码请求
                        try {
                            Buffer encodeBuffer = ProtocolMsgEncoder.encode(protocolMessage);
                            socket.write(encodeBuffer);
                        } catch (IOException e) {
                            throw new RuntimeException("协议消息编码错误");
                        }

                        // 接收响应
                        socket.handler(buffer -> {
                            try {
                                var rpcResponseProtocolMsg = (ProtocolMsg<RPCResp>) ProtocolMsgDecoder.decode(buffer);
                                responseFuture.complete(rpcResponseProtocolMsg.getBody());
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息解码错误");
                            }
                        });
                    } else {
                        System.err.println("Failed to connect to TCP server");
                    }
                });

        var rpcResp = responseFuture.get();
        netClient.close();
        return rpcResp;
    }
}
