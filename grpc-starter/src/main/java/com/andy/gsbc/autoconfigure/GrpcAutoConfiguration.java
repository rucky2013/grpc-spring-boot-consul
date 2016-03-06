package com.andy.gsbc.autoconfigure;

import com.andy.gsbc.GrpcServerRunner;
import com.andy.gsbc.GrpcService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 延泽 on 3/6 0006.
 * GrpcAutoConfiguration
 */
@Configuration
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcAutoConfiguration {
    @Bean
    @ConditionalOnBean(annotation = GrpcService.class)
    public GrpcServerRunner grpcServerRunner(){
        return new GrpcServerRunner();
    }
}
