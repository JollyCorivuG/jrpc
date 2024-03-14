package bupt.edu.jhc.jrpc.serializer;

import java.io.*;

/**
 * @Description: JDK 序列化器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class JDKSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        var outputStream = new ByteArrayOutputStream();
        try (var objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(obj);
        }
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        var inputStream = new ByteArrayInputStream(data);
        try (var objectInputStream = new ObjectInputStream(inputStream)) {
            return clz.cast(objectInputStream.readObject());
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}
