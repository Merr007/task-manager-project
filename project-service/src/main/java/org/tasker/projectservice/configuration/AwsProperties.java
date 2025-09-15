package org.tasker.projectservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class AwsProperties {

    private String region;
    private Endpoint endpoint;
    private Credentials credentials;
    private S3 s3;

    @Getter
    @Setter
    public static class Endpoint{
        private String url;
    }

    @Getter
    @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter
    @Setter
    public static class S3 {
        private String bucket;
        private long signatureDuration;
    }
}
