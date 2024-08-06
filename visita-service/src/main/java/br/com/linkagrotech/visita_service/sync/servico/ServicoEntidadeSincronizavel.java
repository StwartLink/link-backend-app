package br.com.linkagrotech.visita_service.sync.servico;



import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoException;
import br.com.linkagrotech.visita_service.sync.exception.TipoSincronizacaoErro;
import br.com.linkagrotech.visita_service.sync.modelo.*;
import br.com.linkagrotech.visita_service.sync.repositorio.RepositorioEntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.util.SyncUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class ServicoEntidadeSincronizavel {

    private final RepositorioEntidadeSincronizavel repositorioEntidadeSincronizavel;

    public ServicoEntidadeSincronizavel(RepositorioEntidadeSincronizavel repositorioEntidadeSincronizavel){
        this.repositorioEntidadeSincronizavel = repositorioEntidadeSincronizavel;
    }


    @Transactional
    public Changes pullEntities(PullRequestWrapper pullRequest, List<Class<? extends EntidadeSincronizavel>> classes) throws SincronizacaoException {
        Changes changes = new Changes();

        changes.setTableChanges(new HashMap<>());

        if(pullRequest.shcemaVersion == null|| SyncUtil.obterSchemaVersion(EntidadeSincronizavel.class)!=pullRequest.shcemaVersion ){
            throw new SincronizacaoException(TipoSincronizacaoErro.VERSAO_SCHEMA_DIFERENTE);
        }

        for (Class<? extends EntidadeSincronizavel> clazz : classes) {



            String tableName = SyncUtil.obterTableNameFromEntity(clazz);

            Change change = new Change();

            if(pullRequest.lastPulledAt==null || pullRequest.lastPulledAt==0){
                change.setCreated(this.repositorioEntidadeSincronizavel.obterTodos(clazz));
                change.setUpdated(Collections.emptyList());
                change.setDeleted( Collections.emptyList());
                changes.getTableChanges().put(tableName,change);
            }

            change.setCreated(repositorioEntidadeSincronizavel.obterCreatedSince(pullRequest.lastPulledAt, clazz));
            change.setUpdated(repositorioEntidadeSincronizavel.obterUpdatedSince(pullRequest.lastPulledAt,clazz));
            change.setDeleted(repositorioEntidadeSincronizavel.obterDeletedSince(pullRequest.lastPulledAt,clazz));

            changes.getTableChanges().put(tableName,change);
        }

     return changes;
    }

    @Transactional
    public void pushEntities(ChangesWrapper wrapper, List<Class<? extends EntidadeSincronizavel>> classes) throws SincronizacaoException {

        Changes changes = wrapper.getChanges();

        for (Class<? extends EntidadeSincronizavel> clazz : classes) {

            var change = changes.getTableChanges().get(SyncUtil.obterTableNameFromEntity(clazz));

            if(change!=null && change.possuiAlteracoes()){
                var createds = change.getCreated();
                var updates = change.getUpdated();
                var deleted = change.getDeleted();

                if(createds!=null && !createds.isEmpty())
                    repositorioEntidadeSincronizavel.salvarSincronizaveis(createds, clazz);

                if(updates!=null && !updates.isEmpty()) {
                    repositorioEntidadeSincronizavel.verificarConflitoUpdate(updates,wrapper.getLastPulledAt(),clazz);
                    repositorioEntidadeSincronizavel.atualizarSincronizaveis(updates, clazz);
                }

                if(deleted!=null && !deleted.isEmpty()) {
                    repositorioEntidadeSincronizavel.verificarConflitoDeletar(deleted, wrapper.getLastPulledAt(),clazz);
                    repositorioEntidadeSincronizavel.deletarSincronizaveis(deleted, clazz);
                }
            }

        }


    }


}
