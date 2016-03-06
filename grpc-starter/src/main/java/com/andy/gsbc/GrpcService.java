package com.andy.gsbc;

import com.andy.gsbc.register.ServiceInfo;

/**
 *
 * Marks the annotated class to be registered as grpc-service bean;
 * @author  Furer Alexander
 * @since 0.0.1
 */
public @interface GrpcService {
    /**
     *
     * @return protoc-generated class that creates {@link io.grpc.ServerServiceDefinition} via static <code>bindService</code> function.
     */
    Class<?> grpcServiceOuterClass();
}
