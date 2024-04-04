package bupt.edu.jhc.jrpc.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 协议消息的类型枚举
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
@AllArgsConstructor
@Getter
public enum ProtocolMsgTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final Integer type;

    public static final Map<Integer, ProtocolMsgTypeEnum> cache;

    static {
        cache = Arrays.stream(ProtocolMsgTypeEnum.values()).collect(Collectors.toMap(ProtocolMsgTypeEnum::getType, Function.identity()));
    }

    /**
     * 根据类型码获取类型枚举
     *
     * @param type
     * @return
     */
    public static ProtocolMsgTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
