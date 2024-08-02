package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "visita")
@Data
public class Visita extends EntidadeSincronizavel {

    public static final int SCHEMA_VERSION = 1;

    @OneToOne
    private TipoVisita tipoVisita;

    @ManyToOne
    private Cliente cliente;

}
