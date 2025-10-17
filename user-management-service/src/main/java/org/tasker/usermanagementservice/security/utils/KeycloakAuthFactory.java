package org.tasker.usermanagementservice.security.utils;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tasker.usermanagementservice.api.dto.auth.LoginRequest;

@Component
public class KeycloakAuthFactory {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String tokenUrl;

    private KeycloakAuthFactory() {}

    public Keycloak of(LoginRequest loginRequest) {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(loginRequest.username())
                .password(loginRequest.password())
                .resteasyClient(ClientBuilder.newBuilder().build())
                .build();
    }

    public Response ofRefresh(String refreshToken) {
        ResteasyClient resteasyClient = (ResteasyClient) ResteasyClientBuilder.newBuilder().build();
        WebTarget target = resteasyClient.target(tokenUrl);

        Form form = new Form();
        form.param(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN);
        form.param(OAuth2Constants.REFRESH_TOKEN, refreshToken);
        form.param(OAuth2Constants.CLIENT_ID, clientId);
        form.param(OAuth2Constants.CLIENT_SECRET, clientSecret);

        return target.request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }
}
