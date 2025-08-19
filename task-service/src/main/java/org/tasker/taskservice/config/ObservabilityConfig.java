package org.tasker.taskservice.config;

import io.micrometer.tracing.SpanName;
import org.springframework.boot.actuate.autoconfigure.tracing.ConditionalOnEnabledTracing;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnEnabledTracing
public class ObservabilityConfig {
    // Раздел оставлен под дополнительные кастомизации (Span Handlers, Baggage и т.п.)
    @SpanName("task-service")
    public String serviceSpanName() { return "task-service"; }
}
