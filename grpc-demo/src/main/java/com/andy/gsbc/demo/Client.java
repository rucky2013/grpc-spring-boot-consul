package com.andy.gsbc.demo;

import com.andy.example.grpc.helloworld.GreeterGrpc;
import com.andy.example.grpc.helloworld.HelloReply;
import com.andy.example.grpc.helloworld.HelloRequest;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xieyz on 2016/3/7.
 * Client
 */

@SpringBootApplication
public class Client {

    static AtomicInteger total = new AtomicInteger(0);
    static int total1 = 0;

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel managedChannel;
        GreeterGrpc.GreeterBlockingStub blockingStub;
        HelloRequest request = HelloRequest.newBuilder().setName("aaa").build();
        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("192.168.118.152:8500")).build();
        HealthClient healthClient = consul.healthClient();

        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances("DemoServer").getResponse();

        System.out.println(nodes.get(1));
        managedChannel =
                NettyChannelBuilder.forAddress(nodes.get(0).getService().getAddress(), nodes.get(0).getService().getPort()).usePlaintext(true).build();
        blockingStub = GreeterGrpc.newBlockingStub(managedChannel);

        try {
            HelloReply helloReply = blockingStub.sayHello(request);
            System.out.println(helloReply);
        }catch (Exception ex){
            ex.printStackTrace();
        }



        int threadCount = 1;

//        for (int i = 0; i < threadCount; i++) {
//            new Thread(() -> {
//                while (true) {
//                    blockingStub.sayHello(request);
//                    System.out.println("1111111111111111111");
//                    total1 = Client.total.getAndIncrement();
//                }
//            }).start();
//        }
//
//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(1000);
//                    System.out.println(total1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        //Thread.sleep(100000, 111);
    }
}
