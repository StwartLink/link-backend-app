package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import jakarta.persistence.*;

@Entity(name = "visita")
public class Visita extends EntidadeSincronizavel {

    @OneToOne
    public TipoVisita tipoVisita;

    @ManyToOne
    public Cliente cliente;

}
