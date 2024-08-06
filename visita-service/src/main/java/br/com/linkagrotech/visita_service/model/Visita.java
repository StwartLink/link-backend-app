package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "visita")
@Data
@NoArgsConstructor
public class Visita extends EntidadeSincronizavel {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_visita_id")
    private TipoVisita tipoVisita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String observacao;

    @Transient
    private UUID tipo_visita_id;

    @Transient
    private UUID agendamento_id;

    @Transient
    private UUID cliente_id;

}
