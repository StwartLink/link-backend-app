package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity(name="agendamento")
@Data
public class Agendamento extends EntidadeSincronizavel {

    /**
     * Visitas que já ocorreram ou que já possuem dias específicos programados para ocorrer
     */
    @OneToMany(mappedBy = "agendamento",fetch = FetchType.LAZY)
    private List<Visita> visitas;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recorrencia_id")
    private Recorrencia recorrencia;

    @Transient
    private UUID recorrencia_id;

    private String observacoes;

}
