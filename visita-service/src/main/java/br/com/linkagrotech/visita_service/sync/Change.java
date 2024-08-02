package br.com.linkagrotech.visita_service.sync;

import lombok.Data;

import java.util.List;

@Data
public class Change<T extends EntidadeSincronizavel> {

    List<T> created;

    List<T> updated;

    List<String> deleted;

}
