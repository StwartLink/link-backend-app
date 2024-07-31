package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name="cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

}
