package br.com.linkagrotech.auth_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/")
@RestController
@Slf4j
public class TokenController {

    @Value("${link-gateway-oauth.issuer-uri}")
    private String issuerUri;

    @Value("${link-gateway-oauth.clientId}")
    private String clientId;

    @Value("${link-gateway-oauth.clientSecret}")
    private String clientSecret;

    @Operation(summary = "Request Token", description = "Login ", security = @SecurityRequirement(name = "security_auth"))
    @PostMapping("/token")
    public ResponseEntity<String> token(@RequestBody User user) {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("username", user.username());
        formData.add("password", user.password());
        formData.add("grant_type", "password");
        formData.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        try {
            var result = rt.postForEntity(issuerUri + "/protocol/openid-connect/token",
                    entity,
                    String.class);
            return result;
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception: ", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    public record User(String username, String password) {
    }
}
