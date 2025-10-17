package org.tasker.common.grpc.client.interceptor;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tasker.common.security.GrpcClientOAuthManager;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenGrpcClientInterceptor implements ClientInterceptor {

    private final GrpcClientOAuthManager grpcClientOAuthManager;

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions,
                                                               Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(
                channel.newCall(methodDescriptor, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                try {
                    String token = grpcClientOAuthManager.authorizeBeforeCall();
                    if (token != null && !token.isEmpty()) {
                        headers.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER),
                                "Bearer " + token);
                        log.debug("Added JWT token to gRPC call for method: {}",
                                methodDescriptor.getFullMethodName());
                    } else {
                        log.warn("No JWT token available for gRPC call to method: {}",
                                methodDescriptor.getFullMethodName());
                    }
                } catch (Exception e) {
                    log.error("Failed to add JWT token to gRPC call", e);
                }

                super.start(responseListener, headers);
            }
        };
    }
}
