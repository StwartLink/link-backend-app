package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "limite_credito")
@Data
public class LimiteCredito extends EntidadeSincronizavel {

    private BigDecimal limite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Transient
    private UUID cliente_id;

}
