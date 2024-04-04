package bupt.edu.jhc.jrpc.protocol;

/**
 * @Description: 协议常数
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
public interface ProtocolConstants {
    int MESSAGE_HEADER_LENGTH = 17; // 消息头长度 (单位: 字节)
    byte PROTOCOL_MAGIC = 0x1; // 协议魔数
    byte PROTOCOL_VERSION = 0x1; // 协议版本
}
