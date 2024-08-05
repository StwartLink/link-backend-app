package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "visita")
@Data
@NoArgsConstructor
public class Visita extends EntidadeSincronizavel {

    public static final int SCHEMA_VERSION = 1;


    @ManyToOne(cascade = CascadeType.ALL)
    private TipoVisita tipoVisita;

    @ManyToOne
    private Cliente cliente;

    String observacao;

}
