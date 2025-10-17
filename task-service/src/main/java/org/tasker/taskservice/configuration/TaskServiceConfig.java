package org.tasker.taskservice.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tasker.common.grpc.ProjectServiceGrpc;
import org.tasker.common.grpc.client.configuration.CommonGrpcClientConfig;
import org.tasker.common.grpc.client.interceptor.JwtTokenGrpcClientInterceptor;
import org.tasker.common.s3.configuration.S3AwsConfig;
import org.tasker.common.s3.properties.AwsProperties;

@Configuration
@Import({
        S3AwsConfig.class,
        AwsProperties.class,
        CommonGrpcClientConfig.class
})
public class TaskServiceConfig {

    @Value("${grpc.client.project-service.address}")
    private String projectServiceUrl;

    @Bean
    public ManagedChannel projectServiceManagedChannel(JwtTokenGrpcClientInterceptor jwtTokenGrpcClientInterceptor) {
        return ManagedChannelBuilder.forTarget(projectServiceUrl)
                .usePlaintext()
                .intercept(jwtTokenGrpcClientInterceptor)
                .build();
    }

    @Bean
    public ProjectServiceGrpc.ProjectServiceBlockingStub projectServiceBlockingStub(ManagedChannel projectServiceManagedChannel) {
        return ProjectServiceGrpc.newBlockingStub(projectServiceManagedChannel);
    }
}
