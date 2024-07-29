package br.com.linkagrotech.auth_service.controller;

import br.com.linkagrotech.auth_service.dto.ExcecaoDTO;
import br.com.linkagrotech.auth_service.service.KeycloackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    private KeycloackService keycloackService;



    @PostMapping("/enviar-email-recuperacao")
    public ResponseEntity<String> sendLinkResetPasswordEmail(@RequestBody SendEmailRecord record) throws Exception {


        String listaString = keycloackService.getUsers(record.email());

        AtomicReference<String> idUser = new AtomicReference<>("");

        ObjectMapper objectMapper = new ObjectMapper();

        LinkedList<LinkedHashMap> usuarios = objectMapper.readValue(listaString, LinkedList.class);

        usuarios.forEach(user->{
            if(user.get("email").toString().equals(record.email()))
                idUser.set(user.get("id").toString());
        });

        keycloackService.executeActionEmail(idUser.get());

        return ResponseEntity.ok("");
    }


    @PostMapping("/refresh-token")
    public ResponseEntity refreshToken(@RequestBody RefreshTokenRecord wrapper){

        if(wrapper.refreshToken==null || wrapper.refreshToken.equals(""))
            return ResponseEntity.badRequest().body(
                    new ExcecaoDTO("Erro na operação","Não foi possível extratir o header Authorization")
            );

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("grant_type", "refresh_token");
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token",wrapper.refreshToken);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData,headers);

        try {
            return restTemplate.postForEntity(issuerUri + "/protocol/openid-connect/token",
                    entity,
                    String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: "+ e.getMessage());
        }
    }




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
            return rt.postForEntity(issuerUri + "/protocol/openid-connect/token",
                    entity,
                    String.class);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception: ", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    public record User(String username, String password) {}


    public record RefreshTokenRecord(String refreshToken){}

    public record SendEmailRecord(String email){}

}
