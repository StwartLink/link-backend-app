package br.com.linkagrotech.userservice.controller;

import br.com.linkagrotech.userservice.dto.UsuarioRecord;
import br.com.linkagrotech.userservice.modelo.Usuario;
import br.com.linkagrotech.userservice.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class Me {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @GetMapping
    public ResponseEntity<Usuario> me(){

        UsuarioRecord usuarioRecord= (UsuarioRecord)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var usr = usuarioRepositorio.findByUsername(usuarioRecord.username());


        return ResponseEntity.ok(usr);
    }



}
