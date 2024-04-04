package bupt.edu.jhc.jrpc.server;

import io.vertx.core.Vertx;

/**
 * @Description: Vert.x HTTP 服务器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public class VertxHttpServer implements Server {
    @Override
    public void start(int port) {
        // 1.创建 Vert.x 实例
        var vertx = Vertx.vertx();

        // 2.创建 HTTP 服务器
        var server = vertx.createHttpServer();

        // 3.监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        // 4.启动 HTTP 服务器
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("HTTP 服务器启动成功，端口：" + port);
            } else {
                System.err.println("HTTP 服务器启动失败：" + result.cause());
            }
        });
    }
}
