package bupt.edu.jhc.jrpc.server;

/**
 * @Description: HTTP 服务器接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/14
 */
public interface HttpServer {
    /**
     * 启动服务器
     *
     * @param port
     */
    void start(int port);
}
