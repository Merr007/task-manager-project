package org.tasker.usermanagementservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tasker.common.grpc.server.configuration.GrpcServerConfig;

@Configuration
@Import({GrpcServerConfig.class})
public class UserManagementServiceConfig {
}
