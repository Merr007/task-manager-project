package org.tasker.common.grpc.server.interceptor;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * gRPC interceptor for internal client-server call's authentication. Validates client's jwt tokens using Keycloak.
 */
@Slf4j
@RequiredArgsConstructor
public class GrpcAuthServerInterceptor implements ServerInterceptor {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {

        try {
            String authHeader = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid authorization header in gRPC call to {}", call.getMethodDescriptor().getFullMethodName());
                call.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid authorization header"), new Metadata());
                return new ServerCall.Listener<>() {
                };
            }

            String token = authHeader.substring(7);

            Authentication authentication = validateAndCreateAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("gRPC call authenticated for method: {} with principal: {}",
                    call.getMethodDescriptor().getFullMethodName(),
                    authentication.getName());

            return next.startCall(call, headers);

        } catch (Exception e) {
            log.error("Error during gRPC authentication", e);
            call.close(Status.INTERNAL.withDescription("Authentication error"), new Metadata());
            return new ServerCall.Listener<>() {
            };
        }
    }

    /**
     * Validate JWT token using Keycloak authentication server and creates Authentication
     */
    private Authentication validateAndCreateAuthentication(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);

            return jwtAuthenticationConverter.convert(jwt);

        } catch (Exception e) {
            log.warn("Failed to validate JWT token", e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
