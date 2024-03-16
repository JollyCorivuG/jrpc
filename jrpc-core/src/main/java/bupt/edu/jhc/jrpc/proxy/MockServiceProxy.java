package bupt.edu.jhc.jrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description: mock 服务代理类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/16
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var returnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }

        return switch (type.getTypeName()) {
            case "int" -> 0;
            case "long" -> 0L;
            case "short" -> (short) 0;
            case "byte" -> (byte) 0;
            case "float" -> 0.0F;
            case "double" -> 0.0D;
            case "char" -> '\u0000';
            case "boolean" -> false;
            default -> null;
        };
    }
}
