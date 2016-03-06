package com.andy.gsbc.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 延泽 on 3/6 0006.
 * GrpcServerProperties
 */
@ConfigurationProperties("grpc")
public class GrpcServerProperties {
    /**
     * gRPC server port
     */
    private int port = 6565;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
