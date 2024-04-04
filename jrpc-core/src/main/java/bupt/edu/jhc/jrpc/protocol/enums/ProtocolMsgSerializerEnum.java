package bupt.edu.jhc.jrpc.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 协议消息的序列化器枚举
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
@AllArgsConstructor
@Getter
public enum ProtocolMsgSerializerEnum {
    JDK(0, "jdk"),
    KRYO(1, "kryo"),
    HESSIAN(2, "hessian");

    private final Integer key;
    private final String value;

    public static final Map<Integer, ProtocolMsgSerializerEnum> cache1;
    public static final Map<String, ProtocolMsgSerializerEnum> cache2;

    static {
        cache1 = Arrays.stream(ProtocolMsgSerializerEnum.values()).collect(Collectors.toMap(ProtocolMsgSerializerEnum::getKey, Function.identity()));
        cache2 = Arrays.stream(ProtocolMsgSerializerEnum.values()).collect(Collectors.toMap(ProtocolMsgSerializerEnum::getValue, Function.identity()));
    }

    /**
     * 根据序列化器码获取序列化器枚举
     *
     * @param key
     * @return
     */
    public static ProtocolMsgSerializerEnum of(Integer key) {
        return cache1.get(key);
    }

    /**
     * 根据序列化器名获取序列化器枚举
     *
     * @param value
     * @return
     */
    public static ProtocolMsgSerializerEnum of(String value) {
        return cache2.get(value);
    }

}
