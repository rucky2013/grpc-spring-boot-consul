package com.andy.gsbc;

import com.andy.gsbc.autoconfigure.GrpcServerProperties;
import com.andy.gsbc.register.RpcRegister;
import com.andy.gsbc.register.ServiceInfo;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by 延泽 on 3/6 0006.
 * runner
 */
@Component
public class GrpcServerRunner implements CommandLineRunner, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(GrpcServerRunner.class);

    /**
     * Name of static function of gRPC service-outer class that creates {@link io.grpc.ServerServiceDefinition}.
     */
    final private static String bindServiceMethodName = "bindService";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GrpcServerProperties gRpcServerProperties;

    @Autowired
    private RpcRegister rpcRegister;

    private Server server;

    public GrpcServerRunner() {
    }

    public void run(String... strings) throws Exception {
        final ServerBuilder<?> serverBuilder = ServerBuilder.forPort(gRpcServerProperties.getPort());
        for (Object grpcService : applicationContext.getBeansWithAnnotation(GrpcService.class).values()) {
            final Class grpcServiceOuterClass =
                    AnnotationUtils.findAnnotation(grpcService.getClass(), GrpcService.class).grpcServiceOuterClass();
            final String serviceName =
                    AnnotationUtils.findAnnotation(grpcService.getClass(), GrpcService.class).serviceName();
            String version = AnnotationUtils.findAnnotation(grpcService.getClass(), GrpcService.class).version();

            final Optional<Method> bindServiceMethod =
                    Arrays.asList(ReflectionUtils.getAllDeclaredMethods(grpcServiceOuterClass)).stream().filter(method ->
                            bindServiceMethodName.equals(method.getName()) && 1 == method.getParameterCount()
                                    && method.getParameterTypes()[0].isAssignableFrom(grpcService.getClass())).findFirst();

            if (bindServiceMethod.isPresent()) {
                ServerServiceDefinition serviceDefinition =
                        (ServerServiceDefinition) bindServiceMethod.get().invoke(null, grpcService);
                serverBuilder.addService(serviceDefinition);

                ServiceInfo serviceInfo = new ServiceInfo(serviceName,gRpcServerProperties.getPort(),version);
                rpcRegister.registerRpc(serviceInfo);
                logger.info("'{}' service has been registered.", serviceDefinition.getName());
            } else {
                throw new IllegalArgumentException(String.format("Failed to find '%s' method on class %s.\r\n" +
                        "Please make sure you've provided correct 'grpcServiceOuterClass' attribute for '%s' annotation.\r\n"
                        +
                        "It should be the protoc-generated outer class of your service.", bindServiceMethodName, grpcServiceOuterClass.getName(), GrpcService.class.getName()));
            }
        }

        server = serverBuilder.build().start();

        logger.info("gRPC Server started, listening on port {}.", gRpcServerProperties.getPort());
        startDaemonAwaitThread();
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread() {
            @Override
            public void run() {
                try {
                    GrpcServerRunner.this.server.awaitTermination();
                } catch (InterruptedException e) {
                    logger.error("gRPC server stopped.", e);
                }
            }

        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void destroy() throws Exception {
        logger.info("Shutting down gRPC server ...");
        Optional.ofNullable(server).ifPresent(Server::shutdown);
        logger.info("gRPC server stopped.");
    }
}
