package br.com.linkagrotech.visita_service.sync;

import lombok.Data;

import java.util.List;

@Data
public class Change {

    List<EntidadeSincronizavel> created;

    List<EntidadeSincronizavel> updated;

    List<String> deleted;

}
