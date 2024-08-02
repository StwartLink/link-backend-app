package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "visita")
@Data
@NoArgsConstructor
@JsonTypeName("visita")
public class Visita extends EntidadeSincronizavel {

    public static final int SCHEMA_VERSION = 1;

    // todo debugar como vai funcionar o update propagado
    @OneToOne(cascade = CascadeType.MERGE)
    private TipoVisita tipoVisita;

    @ManyToOne
    private Cliente cliente;

}
