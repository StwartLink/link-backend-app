package br.com.linkagrotech.userservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCriadoRecord {
    String id;
    String username;
    String nome;
    String sobrenome;
    String telefone;
    String email;
    String celular;
}
