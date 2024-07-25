package br.com.linkagrotech.userservice.dto;

import lombok.Builder;

@Builder
public record UsuarioCriadoRecord(
        String id,
        String username,
        String nome,
        String sobrenome,
        String telefone,
        String email,
        String celular
) {
}
