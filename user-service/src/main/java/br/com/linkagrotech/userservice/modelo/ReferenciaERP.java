package br.com.linkagrotech.userservice.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "referencia_erp")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReferenciaERP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String valor;

    private TipoReferencia tipoReferencia;


    @ManyToOne
    Usuario usuario;

}
