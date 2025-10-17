package org.tasker.common.grpc.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.tasker.common.grpc.client.interceptor.JwtTokenGrpcClientInterceptor;
import org.tasker.common.security.GrpcClientOAuthManager;

@Configuration
public class CommonGrpcClientConfig {

    @Bean
    public JwtTokenGrpcClientInterceptor jwtTokenGrpcClientInterceptor(GrpcClientOAuthManager grpcClientOAuthManager) {
        return new JwtTokenGrpcClientInterceptor(grpcClientOAuthManager);
    }

    @Bean
    public GrpcClientOAuthManager grpcClientOAuthManager(OAuth2AuthorizedClientManager authorizedClientManager) {
        return new GrpcClientOAuthManager(authorizedClientManager);
    }
}
