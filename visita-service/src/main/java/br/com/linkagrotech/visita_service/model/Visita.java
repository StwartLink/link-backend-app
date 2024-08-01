package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity(name = Visita.TABLE_NAME)
@Data
public class Visita extends EntidadeSincronizavel {

    public static final String TABLE_NAME = "visita";

    @OneToOne
    TipoVisita tipoVisita;

    @ManyToOne
    Cliente cliente;

}
