package br.com.linkagrotech.userservice.controller;

import br.com.linkagrotech.userservice.dto.UsuarioCriadoRecord;
import br.com.linkagrotech.userservice.service.ServicoKeycloack;
import br.com.linkagrotech.userservice.dto.ExcecaoDTO;
import br.com.linkagrotech.userservice.dto.UsuarioCadastroRecord;
import br.com.linkagrotech.userservice.dto.UsuarioNovoKeycloak;
import br.com.linkagrotech.userservice.modelo.Usuario;
import br.com.linkagrotech.userservice.repository.UsuarioRepositorio;
import brave.internal.Nullable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController()
@RequestMapping("/gerenciar")
public class GerenciarUsuarioController {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    ServicoKeycloack servicoKeycloack;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@RequestBody @Nullable UsuarioCadastroRecord record){

        var usuarioSalvar = Usuario.builder()
                .username(record.username())
                .email(record.email())
                .telefone(record.telefone())
                .celular(record.celular())
                .nome(record.nome())
                .sobrenome(record.sobrenome())
                .build();

        if(usuarioRepositorio.existsByUsernameOrEmail(usuarioSalvar.getUsername(),usuarioSalvar.getEmail()))
            return ResponseEntity.badRequest().body(new ExcecaoDTO("Usuário já cadastrado!","Username ou email já existente"));

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> responseEntity;

        try {
            headers.setContentType(MediaType.APPLICATION_JSON);
            //todo trocar para token do admin do agrobasil somente
            headers.setBearerAuth(servicoKeycloack.getKeyCloackToken());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ExcecaoDTO("Não foi possível se autenticar no keycloack","erro na autenticação do mirosserviço"));
        }


        try{
            responseEntity = servicoKeycloack.cadastrarUsuario(record,headers);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new ExcecaoDTO("Não foi possível criar o usuário no keycloack",
                    e.getMessage()));
        }


        usuarioSalvar = usuarioRepositorio.save(usuarioSalvar);

        URI uri = responseEntity.getHeaders().getLocation();

        //Cadastrar senha do usuário
        try{
            if(uri==null)
                throw new IllegalArgumentException("Usuário não foi criado");

            String[] parts =uri.toString().split("\\/");

            String userKeycloackUUID= parts[parts.length-1];

            servicoKeycloack.atualizarSenhaUsuario(userKeycloackUUID,record.password(),headers);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    UsuarioCriadoRecord.builder()
                            .username(usuarioSalvar.getUsername())
                            .nome(usuarioSalvar.getNome())
                            .sobrenome(usuarioSalvar.getSobrenome())
                            .telefone(usuarioSalvar.getTelefone())
                            .celular(usuarioSalvar.getCelular())
                            .email(usuarioSalvar.getEmail())
                            .id(usuarioSalvar.getId().toString())

            );
        }catch (Exception e){
            usuarioRepositorio.delete(usuarioSalvar);
            if(uri!=null) {
                String[] parts =uri.toString().split("\\/");
                String userKeycloackUUID= parts[parts.length-1];
                servicoKeycloack.deletarUsuario(userKeycloackUUID,headers);
            }

            return ResponseEntity.internalServerError().body(
                    new ExcecaoDTO("Não foi possível definir a senha do usuário. Usuário excluído do Keycloack e do microsserviço de usuário",e.getMessage()));
        }

    }



}
