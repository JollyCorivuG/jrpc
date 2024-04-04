package bupt.edu.jhc.jrpc.server.tcp;

import bupt.edu.jhc.jrpc.protocol.ProtocolConstants;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @Description: 装饰者模式, 对原有的 buffer 处理能力进行增强
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {
    private final RecordParser recordParser;

    /**
     * 构造函数, 对传入的 buffer 处理器进行增强
     *
     * @param bufferHandler buffer 处理器
     */
    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    /**
     * 初始化 RecordParser
     *
     * @param bufferHandler buffer 处理器
     * @return RecordParser 实例
     */
    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        // 构造 parser
        var parser = RecordParser.newFixed(ProtocolConstants.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<>() {
            // 初始化
            private int size = -1;
            // 一次完整的读取（头 + 体）
            private Buffer resultBuffer = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if (-1 == size) {
                    // 读取消息体长度
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    // 写入头信息到结果
                    resultBuffer.appendBuffer(buffer);
                } else {
                    // 写入体信息到结果
                    resultBuffer.appendBuffer(buffer);
                    // 已拼接为完整 Buffer，执行处理
                    bufferHandler.handle(resultBuffer);
                    // 重置一轮
                    parser.fixedSizeMode(ProtocolConstants.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });

        return parser;
    }
}
