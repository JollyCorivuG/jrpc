package bupt.edu.jhc.jrpc.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 协议消息的状态枚举
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
@AllArgsConstructor
@Getter
public enum ProtocolMsgStatusEnum {
    OK(20, "ok"),

    BAD_REQUEST(40, "badRequest"),
    BAD_RESPONSE(50, "badRequest");

    private final Integer code;
    private final String desc;

    private static final Map<Integer, ProtocolMsgStatusEnum> cache;

    static {
        cache = Arrays.stream(ProtocolMsgStatusEnum.values()).collect(Collectors.toMap(ProtocolMsgStatusEnum::getCode, Function.identity()));
    }

    /**
     * 根据状态码获取状态枚举
     *
     * @param code
     * @return
     */
    public static ProtocolMsgStatusEnum of(Integer code) {
        return cache.get(code);
    }
}
