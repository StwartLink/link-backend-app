package br.com.linkagrotech.userservice.service;

import br.com.linkagrotech.userservice.dto.ExcecaoDTO;
import br.com.linkagrotech.userservice.dto.KeyloackCredenciais;
import br.com.linkagrotech.userservice.dto.UsuarioCadastroRecord;
import br.com.linkagrotech.userservice.dto.UsuarioNovoKeycloak;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class ServicoKeycloack {

    private static final String KEYCLOAK_URL = "http://localhost:8081";
    private static final String REALM = "master";
    private static final String CLIENT_ID = "admin-cli";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";


    public  String getKeyCloackToken() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", CLIENT_ID);
        formData.add("username", USERNAME);
        formData.add("password", PASSWORD);
        formData.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        String tokenUrl = KEYCLOAK_URL + "/realms/" + REALM + "/protocol/openid-connect/token";
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
        }
    }

    public KeyloackCredenciais getCredencialRepresentation(String novaSenha){
        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.createObjectNode();
        json.put("value",novaSenha);
        json.put("temporary","false");
        json.put("userLabel","Definida");

        return KeyloackCredenciais.builder().value(novaSenha).temporary("false").userLabel("Senha definida").build();
    }


    public ResponseEntity<String> cadastrarUsuario(UsuarioCadastroRecord record,HttpHeaders headers) throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        String firstName = record.getNome().split(" ")[0];
        String lastname = record.getNome().replace(firstName,"");

        var responseEntity = restTemplate.postForEntity("http://localhost:8081/admin/realms/agrobrasil/users",
                new HttpEntity<>(UsuarioNovoKeycloak.builder()
                        .username(record.getUsername())
                        .email(record.getEmail())
                        .firstName(firstName)
                        .lastName(lastname)
                        .enabled(true)
                        .build(),headers),String.class);
        if(responseEntity.getStatusCode()!=HttpStatus.CREATED)
            throw new Exception("StatusCode do Keycloack: " + responseEntity.getStatusCode());

        return responseEntity;
    }

    public void atualizarSenhaUsuario(String userUUID, String novaSenha, HttpHeaders headers) throws Exception{

        RestTemplate restTemplate = new RestTemplate();


        restTemplate.put(String.format("http://localhost:8081/admin/realms/%s/users/%s/reset-password","agrobrasil",userUUID),
                new HttpEntity<>(this.getCredencialRepresentation(novaSenha),headers),
                String.class
        );

    }

    public void deletarUsuario(String userId, HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(String.format("http://localhost:8081/admin/realms/%s/users/%s", "agrobrasil", userId),
                new HttpEntity<>(headers),
                String.class
        );
    }
}
