package br.com.linkagrotech.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UsuarioCadastroRecord{

        @NotBlank(message = "Username vazio!")
        String username;

        @NotEmpty(message = "Senha vazia!")
        String password;

        @NotBlank(message = "Nome vazio!")
        String nome;

        @NotBlank(message = "Telefone vazio!")
        String telefone;

        @NotBlank(message = "É necessário que o email seja preenchido")
        @Email(message = "É necessário que o email esteja em um formato válido")
        String email;

        String celular;

}
