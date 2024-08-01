package br.com.linkagrotech.visita_service.sync;

import java.util.List;

public class Change<T extends EntidadeSincronizavel> {

    List<T> created;

    List<T> updated;

    List<String> deleted;

}
