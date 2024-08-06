package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name="cliente")
@Data
public class Cliente extends EntidadeSincronizavel {

    String nome;

    @OneToMany(mappedBy = "cliente",fetch = FetchType.LAZY)
    List<LimiteCredito> limiteCredito;

}
