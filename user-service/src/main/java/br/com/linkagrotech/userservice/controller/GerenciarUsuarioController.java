package br.com.linkagrotech.userservice.controller;

import br.com.linkagrotech.userservice.ServicoKeycloack;
import br.com.linkagrotech.userservice.dto.ExcecaoDTO;
import br.com.linkagrotech.userservice.dto.UsuarioCadastroRecord;
import br.com.linkagrotech.userservice.dto.UsuarioNovoKeycloak;
import br.com.linkagrotech.userservice.modelo.Usuario;
import br.com.linkagrotech.userservice.repository.UsuarioRepositorio;
import brave.internal.Nullable;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController()
@RequestMapping("/gerenciar")
public class GerenciarUsuarioController {
    /*


     */

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    ServicoKeycloack servicoKeycloack;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Nullable UsuarioCadastroRecord record){

            var usuarioSalvar = Usuario.builder()
                    .username(record.username())
                    .email(record.email())
                    .telefone(record.telefone())
                    .celular(record.celular())
                    .sobrenome(record.sobrenome())
                    .build();

            if(usuarioRepositorio.existsByUsername(usuarioSalvar.getUsername()))
                return ResponseEntity.badRequest().body(new ExcecaoDTO("Usuário já cadastrado!","Username já existente"));

            HttpHeaders headers = new HttpHeaders();
            RestTemplate rt = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);

            try {
                headers.setBearerAuth(servicoKeycloack.getKeyCloackToken());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(new ExcecaoDTO("Não foi possível se autenticar no keycloack","erro na autenticação do mirosserviço"));
            }

            HttpEntity<UsuarioNovoKeycloak> httpEntity = new HttpEntity<>(UsuarioNovoKeycloak.builder()
                    .username(record.username())
                    .email(record.email())
                    .firstName(record.nome())
                    .lastName(record.sobrenome())
                    .enabled(true)
                    .build(),headers);


            var responseEntity = rt.postForEntity("http://localhost:8081/admin/realms/agrobrasil/users",
                    httpEntity,String.class);


            if(responseEntity.getStatusCode()== HttpStatus.CREATED){
                usuarioSalvar = usuarioRepositorio.save(usuarioSalvar);
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvar);
            }

        return ResponseEntity.internalServerError().body(new ExcecaoDTO("Não foi possível cadastrar o usuário no microsserviço","desconhecido"));

    }



}
