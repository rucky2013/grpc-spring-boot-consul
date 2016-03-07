package com.andy.gsbc.demo;

import com.andy.example.grpc.helloworld.GreeterGrpc;
import com.andy.example.grpc.helloworld.HelloReply;
import com.andy.example.grpc.helloworld.HelloRequest;
import com.andy.gsbc.GrpcService;
import io.grpc.stub.StreamObserver;

/**
 * Created by 延泽 on 3/7 0007.
 * impl
 */
@GrpcService(grpcServiceOuterClass = GreeterGrpc.class, serviceName = "DemoServer", version = "1.0.0")
public class GreeterImpl implements GreeterGrpc.Greeter {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        final HelloReply.Builder replyBuilder = HelloReply.newBuilder().setMessage("Hello " + request.getName());
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }
}
