package br.com.linkagrotech.visita_service.sync.repositorio;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import java.util.Date;
import java.util.List;

public interface RepositorioEntidadeSincronizavel {

    List<EntidadeSincronizavel> obterTodos(Class<? extends EntidadeSincronizavel> entidade);

    List<EntidadeSincronizavel> obterCreatedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade);

    List<EntidadeSincronizavel> obterUpdatedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade);

    List<String> obterDeletedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade);

    void salvarNovosAtualizarVelhos(List<EntidadeSincronizavel> createds, Class<? extends EntidadeSincronizavel> clasz);
}
