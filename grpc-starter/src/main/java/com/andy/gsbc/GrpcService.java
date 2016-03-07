package com.andy.gsbc;

import com.andy.gsbc.register.ServiceInfo;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 *
 * Marks the annotated class to be registered as grpc-service bean;
 * @author  Furer Alexander
 * @since 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface GrpcService {
    /**
     *
     * @return protoc-generated class that creates {@link io.grpc.ServerServiceDefinition} via static <code>bindService</code> function.
     */
    Class<?> grpcServiceOuterClass();

    String serviceName();

    String version();
}
