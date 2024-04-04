package bupt.edu.jhc.jrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: RPC 协议消息结构
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProtocolMsg<T> {
    private Header header; // 消息头
    private T body; // 消息体

    @Data
    public static class Header {
        private byte magic; // 魔数 1 byte
        private byte version; // 版本 1 byte
        private byte serializer; // 序列化器 1 byte
        private byte type; // 消息类型 1 byte
        private byte status; // 状态 1 byte
        private long requestId; // 请求 ID 8 byte
        private int bodyLength; // 消息体长度 4 byte
        // 消息头字节大小: 17 byte
    }
}
