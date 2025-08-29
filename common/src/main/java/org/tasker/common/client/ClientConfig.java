package org.tasker.common.client;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;
import org.tasker.common.client.interceptors.OAuthHttpClientRequestInterceptor;

@Configuration
@AllArgsConstructor
public class ClientConfig {

    private OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    public RestClientUsersRestClient restClientUsersRestClient(@Value("${tasker.services.users.url}") String baseUrl) {
        return new RestClientUsersRestClient(RestClient
                .builder()
                .baseUrl(baseUrl)
                .requestInterceptor(clientHttpRequestInterceptor())
                .build());
    }

    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return new OAuthHttpClientRequestInterceptor(authorizedClientManager);
    }
}
