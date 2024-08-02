package br.com.linkagrotech.visita_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "tipo_visita")
@Data

public class TipoVisita {

    @Id
    @GeneratedValue
    Long id;

    String descricao;


}
