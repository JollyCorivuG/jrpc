package bupt.edu.jhc.jrpc.protocol;

import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.domain.resp.RPCResp;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgSerializerEnum;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgTypeEnum;
import bupt.edu.jhc.jrpc.serializer.Serializer;
import bupt.edu.jhc.jrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @Description: 协议信息解码器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
public class ProtocolMsgDecoder {
    /**
     * 解码
     *
     * @param buffer
     * @return
     * @throws IOException
     */

    public static ProtocolMsg<?> decode(Buffer buffer) throws IOException {
        if (buffer == null || buffer.length() == 0) {
            throw new RuntimeException("消息 buffer 为空");
        }
        if (buffer.getBytes().length < ProtocolConstants.MESSAGE_HEADER_LENGTH) {
            throw new RuntimeException("出现了半包问题");
        }

        // 分别从指定位置读出 Buffer
        var header = new ProtocolMsg.Header();
        var magic = buffer.getByte(0);
        // 校验魔数
        if (magic != ProtocolConstants.PROTOCOL_MAGIC) {
            throw new RuntimeException("消息 magic 非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        // 解决粘包问题，只读指定长度的数据
        var bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        // 解析消息体
        var serializerEnum = ProtocolMsgSerializerEnum.of((int) header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化消息的协议不存在");
        }
        Serializer serializer = SerializerFactory.getSerializer(serializerEnum.getValue());
        var messageTypeEnum = ProtocolMsgTypeEnum.of((int) header.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("序列化消息的类型不存在");
        }
        return switch (messageTypeEnum) {
            case REQUEST -> {
                var request = serializer.deserialize(bodyBytes, RPCReq.class);
                yield ProtocolMsg.builder()
                        .header(header)
                        .body(request)
                        .build();
            }
            case RESPONSE -> {
                var response = serializer.deserialize(bodyBytes, RPCResp.class);
                yield ProtocolMsg.builder()
                        .header(header)
                        .body(response)
                        .build();
            }
            default -> throw new RuntimeException("暂不支持该消息类型");
        };
    }
}
