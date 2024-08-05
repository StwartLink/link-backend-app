package br.com.linkagrotech.visita_service.sync.servico;



import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoException;
import br.com.linkagrotech.visita_service.sync.exception.TipoSincronizacaoErro;
import br.com.linkagrotech.visita_service.sync.modelo.Change;
import br.com.linkagrotech.visita_service.sync.modelo.Changes;
import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.modelo.PullRequestWrapper;
import br.com.linkagrotech.visita_service.sync.repositorio.RepositorioEntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.util.SyncUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;

@Service
public class ServicoEntidadeSincronizavel {

    @Autowired
    private RepositorioEntidadeSincronizavel repositorioEntidadeSincronizavel;


    @SafeVarargs
    public final Changes pullEntities(PullRequestWrapper pullRequest, Class<? extends EntidadeSincronizavel>... classes) {
        Changes changes = new Changes();

        changes.setTableChanges(new HashMap<>());

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

    @SafeVarargs
    @Transactional
    public final void pushEntities(Changes changes, Class<? extends EntidadeSincronizavel>... classes) throws SincronizacaoException {

        for (Class<? extends EntidadeSincronizavel> clazz : classes) {

            var change = changes.getTableChanges().get(SyncUtil.obterTableNameFromEntity(clazz));

            if(change==null)
                throw new SincronizacaoException(TipoSincronizacaoErro.PUSH_INVALIDO_SEM_ALTERACOES,
                        "Sem alterações na tabela exigida para push: "+ SyncUtil.obterTableNameFromEntity(clazz)
                        );

            var createds = change.getCreated();
            var updates = change.getUpdated();
            var deleted = change.getDeleted();

            if(createds!=null && !createds.isEmpty())
                repositorioEntidadeSincronizavel.salvarSincronizaveis(createds, clazz);

            if(updates!=null && !updates.isEmpty())
                repositorioEntidadeSincronizavel.atualizarSincronizaveis(updates,clazz);

            if(deleted!=null && !deleted.isEmpty())
                repositorioEntidadeSincronizavel.deletarSincronizaveis(deleted,clazz);

        }


    }


}
