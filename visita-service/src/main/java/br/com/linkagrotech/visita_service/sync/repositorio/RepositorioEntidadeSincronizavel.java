package br.com.linkagrotech.visita_service.sync.repositorio;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoException;

import java.util.List;

public interface RepositorioEntidadeSincronizavel {

    List<EntidadeSincronizavel> obterTodos(Class<? extends EntidadeSincronizavel> entidade);

    List<EntidadeSincronizavel> obterCreatedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade);

    List<EntidadeSincronizavel> obterUpdatedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade);

    List<String> obterDeletedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade);

    void salvarSincronizaveis(List<EntidadeSincronizavel> createds, Class<? extends EntidadeSincronizavel> clasz);

    void atualizarSincronizaveis(List<EntidadeSincronizavel> updated, Class<? extends EntidadeSincronizavel> clazz) throws SincronizacaoException;

    void deletarSincronizaveis(List<String> deleted, Class<? extends EntidadeSincronizavel> clazz);
}
