package bupt.edu.jhc.jrpc.server.tcp;

import bupt.edu.jhc.jrpc.server.Server;
import io.vertx.core.Vertx;

/**
 * @Description: Vert.x TCP 服务端
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
public class VertxTcpServer implements Server {
    @Override
    public void start(int port) {
        // 1.创建 Vert.x 实例
        var vertx = Vertx.vertx();

        // 2.创建 TCP 服务器
        var server = vertx.createNetServer();

        // 3.处理请求
        server.connectHandler(new TcpServerHandler());

        // 4.启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port " + port);
            } else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }
}
