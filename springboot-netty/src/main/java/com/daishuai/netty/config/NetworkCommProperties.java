package com.daishuai.netty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 通信配置
 * @createTime 2022年08月18日 09:16:00
 */
@Data
@ConfigurationProperties(prefix = "kem.suite.network-comm")
public class NetworkCommProperties {

    private Client client = new Client();

    private Server server = new Server();

    @Data
    public static class Server {

        private int port = 22020;

        private long readIdleTime = 0;
    }

    @Data
    public static class Client {
        private String host = "localhost";

        private int port = 22020;

        private long writeIdleTime = 0;

        private boolean reconnect = true;

        private int reconnectTimes = Integer.MAX_VALUE;

        private int reconnectInterval = 10;
    }
}
