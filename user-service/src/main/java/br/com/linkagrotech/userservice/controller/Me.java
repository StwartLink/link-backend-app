package br.com.linkagrotech.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.linkagrotech.userservice.dto.ExcecaoDTO;
import br.com.linkagrotech.userservice.dto.UsuarioRecord;
import br.com.linkagrotech.userservice.repository.UsuarioRepositorio;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/me")
public class Me {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Operation(summary = "Request Me Infos", description = "/Me")
    @GetMapping
    public ResponseEntity<Object> me() {

	UsuarioRecord usuarioRecord = (UsuarioRecord) SecurityContextHolder.getContext().getAuthentication()
		.getPrincipal();

	var usr = usuarioRepositorio.findByUsername(usuarioRecord.username());

	if (usr == null) {
	    return ResponseEntity.badRequest().body(new ExcecaoDTO("Usuário não existente",
		    "Não encontrado nenhum usuário com esse username no banco"));
	}

	return ResponseEntity.ok(usr);
    }

}
