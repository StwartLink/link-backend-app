package br.com.linkagrotech.visita_service.sync.modelo;

import lombok.Data;

import java.util.List;

@Data
public class Change {

    private List<EntidadeSincronizavel> created;

    private List<EntidadeSincronizavel> updated;

    private List<String> deleted;

}
