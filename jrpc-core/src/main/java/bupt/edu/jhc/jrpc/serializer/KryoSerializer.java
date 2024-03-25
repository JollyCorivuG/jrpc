package bupt.edu.jhc.jrpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Description: Kryo 序列化器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/24
 */
public class KryoSerializer implements Serializer {
    /**
     * Kryo 线程局部变量, 是因为它是线程不安全的
     */
    private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(
            () -> {
                var kryo = new Kryo();
                kryo.setRegistrationRequired(false);
                kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
                return kryo;
            }
    );


    @Override
    public <T> byte[] serialize(T obj) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        try (var output = new Output(byteArrayOutputStream)) {
            KRYO.get().writeObject(output, obj);
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) {
        var byteArrayInputStream = new ByteArrayInputStream(data);
        try (var input = new Input(byteArrayInputStream)) {
            return KRYO.get().readObject(input, clz);
        }
    }
}
