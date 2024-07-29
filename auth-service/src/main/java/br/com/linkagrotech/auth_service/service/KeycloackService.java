package br.com.linkagrotech.auth_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class KeycloackService {

    private static final String REALM = "master";
    private static final String KEYCLOAK_CLIENT_ID = "admin-cli";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String KEYCLOAK_REALM = "agrobrasil";
    private static final String REDIRECT_URL = "http://redirect-link-after-action.com";
    private final RestTemplate restTemplate  = new RestTemplate();

    @Value("${link-gateway-oauth.issuer-uri}")
    private String issuerUrl;

    @Value("${link-gateway-oauth.keyloack-host}")
    private String keyloackHost;

    @Value("${link-gateway-oauth.clientId}")
    private static  String keycloackClientSecret;

    public  String getToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", KEYCLOAK_CLIENT_ID);
        formData.add("username", USERNAME);
        formData.add("password", PASSWORD);
        formData.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        String tokenUrl = keyloackHost + "/realms/" + REALM + "/protocol/openid-connect/token";
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
        }
    }


    public String getUsers(String email) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(getToken());

        String url = keyloackHost + "/admin/realms/" + KEYCLOAK_REALM + "/users";

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody();
    }

    public boolean executeActionEmail(String userId) throws Exception {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        StringBuilder pathBuilder = new StringBuilder();

        URI uri = UriComponentsBuilder.fromHttpUrl(keyloackHost)
                .path(
                        pathBuilder.append("/").append("admin").append("/").append("realms").append("/").append(KEYCLOAK_REALM)
                                .append("/").append("users").append("/").append(userId).append("/").append("reset-password-email").toString()
                )
                .build()
                .toUri();

        HttpEntity<String> request = new HttpEntity<>( headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT)
            return true;

        return false;
    }
}
