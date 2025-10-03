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

@Configuration
@Import({
        ClientConfig.class,
        GrpcServerConfig.class,
        CommonGrpcClientConfig.class,
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
