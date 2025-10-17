package org.tasker.projectservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tasker.common.security.CommonSecurityConfig;

@Configuration
@Import({CommonSecurityConfig.class})
public class SecurityConfig {

}
