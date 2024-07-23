package br.com.linkagrotech.userservice.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.repository.cdi.Eager;

import java.util.List;


@Entity(name = "usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private Long id;

    private String username;

    private String email;

    private String telefone;

    private String celular;

    private byte[] foto;

    private String nome;

    private String sobrenome;

    @OneToMany(mappedBy = "usuario",fetch = FetchType.EAGER)
    private List<ReferenciaERP> referenciasERP;



}
