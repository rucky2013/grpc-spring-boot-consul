package com.andy.gsbc.demo;

import com.andy.example.grpc.helloworld.GreeterGrpc;
import com.andy.example.grpc.helloworld.HelloReply;
import com.andy.example.grpc.helloworld.HelloRequest;
import com.andy.gsbc.GrpcServerRunner;
import com.andy.gsbc.GrpcService;
import com.andy.gsbc.autoconfigure.GrpcAutoConfiguration;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 * Created by 延泽 on 3/6 0006.
 * Demo
 */
@SpringBootApplication
public class DemoServer {

    @GrpcService(grpcServiceOuterClass = GreeterGrpc.class)
    public static class DemoService implements GreeterGrpc.Greeter {

        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            final HelloReply.Builder replyBuilder = HelloReply.newBuilder().setMessage("Hello " + request.getName());
            responseObserver.onNext(replyBuilder.build());
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext text = SpringApplication.run(DemoServer.class, args);
        Map<String,Object> map =text.getBeansWithAnnotation(GrpcService.class);
        text.getBean(GrpcAutoConfiguration.class);
        System.out.println(map.keySet().size());
    }
}
