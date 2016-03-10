package com.andy.gsbc.register;

import com.andy.gsbc.autoconfigure.GrpcServerProperties;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by 延泽 on 3/8 0008.
 * RpcRegisterImpl
 */
@Component
public class RpcRegisterImpl implements RpcRegister {

    private final static Logger logger = LoggerFactory.getLogger(RpcRegister.class);

    @Autowired
    private GrpcServerProperties gRpcServerProperties;

    @Override
    public void registerRpc(ServiceInfo serviceInfo) {
        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString(gRpcServerProperties.getConsulHost())).build();
        AgentClient agentClient = consul.agentClient();
        String ip;
        String serviceId = gRpcServerProperties.getLocalIp() + ":" + gRpcServerProperties.getPort();
        String healthUrl = String.format("http://%s:8080/health", gRpcServerProperties.getLocalIp());
        Registration registration = ImmutableRegistration.builder().address(gRpcServerProperties.getLocalIp()).port(gRpcServerProperties.getPort())
                .id(serviceId).name(serviceInfo.getName()).addTags(serviceInfo.getVersion())
                .check(Registration.RegCheck.http(healthUrl, 5)).build();
        agentClient.register(registration);
        logger.info("grpc service register to consul:{},health url:{}", serviceInfo, healthUrl);
    }

    @Override
    public void unRegisterRpc(String serviceId) {

    }
}
