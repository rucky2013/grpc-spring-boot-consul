package com.andy.gsbc.demo;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.model.agent.Agent;
import com.orbitz.consul.model.health.ServiceHealth;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by xieyz on 2016/3/7.
 * Client
 */

public class Client {

    @Test
    public void clientTest() throws Exception {

        Consul consul = Consul.builder().build();
        HealthClient healthClient = consul.healthClient();

        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances("MyService").getResponse();

        ManagedChannel channel =
                NettyChannelBuilder.forAddress(nodes.get(0).getNode().getAddress(), nodes.get(0).getService().getPort()).usePlaintext(true).build();

        Thread.sleep(100000);
    }
}
