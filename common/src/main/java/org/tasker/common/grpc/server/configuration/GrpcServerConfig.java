package org.tasker.common.grpc.server.configuration;

import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.tasker.common.grpc.server.interceptor.GrpcAuthServerInterceptor;

/**
 * Server-side gRPC configuration
 */
@Configuration
public class GrpcServerConfig {

    @Bean
    public GrpcAuthServerInterceptor grpcAuthInterceptor(JwtDecoder jwtDecoder,
                                                         JwtAuthenticationConverter jwtAuthenticationConverter) {
        return new GrpcAuthServerInterceptor(jwtDecoder, jwtAuthenticationConverter);
    }

    @Bean
    public GlobalServerInterceptorConfigurer globalServerInterceptorConfigurer(GrpcAuthServerInterceptor grpcAuthServerInterceptor) {
        return registry -> registry.add(grpcAuthServerInterceptor);
    }
}
