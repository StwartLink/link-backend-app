package br.com.linkagrotech.userservice.dto;

import lombok.Builder;

@Builder
public record UsuarioNovoKeycloak(
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled
) {
}
