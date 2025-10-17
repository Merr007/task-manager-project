package org.tasker.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.util.Assert;

@RequiredArgsConstructor
public class GrpcClientOAuthManager {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Value("${tasker.auth.client.registration-id}")
    private String registrationId;

    @Value("${tasker.auth.client.principal}")
    private String principal;

    public String authorizeBeforeCall() {
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(
                OAuth2AuthorizeRequest.withClientRegistrationId(registrationId)
                        .principal(principal)
                        .build()
        );
        Assert.notNull(authorizedClient, "AuthorizedClient cannot be null");
        return authorizedClient.getAccessToken().getTokenValue();
    }
}
