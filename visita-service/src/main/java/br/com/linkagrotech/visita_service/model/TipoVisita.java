package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity(name = "tipo_visita")
@Data
public class TipoVisita extends EntidadeSincronizavel {

    String titulo;

    String descricao;

}
