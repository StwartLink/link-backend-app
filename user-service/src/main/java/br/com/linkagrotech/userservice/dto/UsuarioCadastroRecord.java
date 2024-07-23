package br.com.linkagrotech.userservice.dto;

import lombok.Builder;

@Builder
public record UsuarioCadastroRecord(
        String username,
        String password,
        String nome,
        String sobrenome,
        String telefone,
        String email,
        String celular
        ) {
}
