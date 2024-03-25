package bupt.edu.jhc.jrpc.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description: Hessian 序列化器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/24
 */
public class HessianSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var ho = new HessianOutput(byteArrayOutputStream);
        ho.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        var byteArrayInputStream = new ByteArrayInputStream(data);
        var hi = new HessianInput(byteArrayInputStream);
        return clz.cast(hi.readObject(clz));
    }
}
