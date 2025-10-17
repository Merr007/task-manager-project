package org.tasker.projectservice.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tasker.common.client.ClientConfig;
import org.tasker.common.grpc.UserServiceGrpc;
import org.tasker.common.grpc.client.configuration.CommonGrpcClientConfig;
import org.tasker.common.grpc.client.interceptor.JwtTokenGrpcClientInterceptor;
import org.tasker.common.grpc.server.configuration.GrpcServerConfig;
import org.tasker.common.s3.configuration.S3AwsConfig;
import org.tasker.common.s3.properties.AwsProperties;

@Configuration
@Import({
        ClientConfig.class,
        GrpcServerConfig.class,
        CommonGrpcClientConfig.class,
        AwsProperties.class,
        S3AwsConfig.class
})
public class ProjectServiceConfig {

    @Value("${grpc.client.user-management-service.address}")
    private String userServiceUrl;

    @Bean
    public ManagedChannel userServiceManagedChannel(JwtTokenGrpcClientInterceptor jwtTokenGrpcClientInterceptor) {
        return ManagedChannelBuilder.forTarget(userServiceUrl)
                .usePlaintext()
                .intercept(jwtTokenGrpcClientInterceptor)
                .build();
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub projectServiceBlockingStub(ManagedChannel userServiceManagedChannel) {
        return UserServiceGrpc.newBlockingStub(userServiceManagedChannel);
    }
}
