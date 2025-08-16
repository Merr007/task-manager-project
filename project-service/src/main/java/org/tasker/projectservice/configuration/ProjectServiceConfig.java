package org.tasker.projectservice.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"org.tasker.projectservice"})
@PropertySource("classpath:application.properties")
public class ProjectServiceConfig {
}
