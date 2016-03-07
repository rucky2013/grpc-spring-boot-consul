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
    private int port = 9966;

    private String consulHost;

    private String localIp;

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getConsulHost() {
        return consulHost;
    }

    public void setConsulHost(String consulHost) {
        this.consulHost = consulHost;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
