package br.com.linkagrotech.visita_service.sync;



import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.repositorio.RepositorioEntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.util.SyncUtil;

import java.util.Collections;
import java.util.Map;

public abstract class ServicoEntidadeSincronizavel {


    public Map.Entry<String,Change> pullEntities(PullRequestObject pullRequest,Class<? extends EntidadeSincronizavel> clazz) {

        String tableName = SyncUtil.obterTableNameFromEntity(clazz);

        Change change = new Change();

        if(pullRequest.lastPulledAt==null || pullRequest.lastPulledAt==0){
            change.created = this.repositorioSincronizavel().obterTodos(clazz);
            change.updated = Collections.emptyList();
            change.deleted = Collections.emptyList();
            return Map.entry(tableName,change);
        }

        change.created = repositorioSincronizavel().obterCreatedSince(pullRequest.lastPulledAt, clazz);
        change.updated = repositorioSincronizavel().obterUpdatedSince(pullRequest.lastPulledAt,clazz);
        change.deleted = repositorioSincronizavel().obterDeletedSince(pullRequest.lastPulledAt,clazz);

        return  Map.entry(tableName,change);
    }

    public void verifySchemaCompatibility(Long clientSchema) throws RuntimeException {

        if(Visita.SCHEMA_VERSION > clientSchema){
            throw new RuntimeException("A versão do schema do cliente é anterior à versão do servidor!");
        }

    }

    public void pushEntities(Changes changes, Class<Visita> clazz) {
        var change = changes.tableChanges.get(SyncUtil.obterTableNameFromEntity(clazz));

        if(change==null)
            throw new IllegalArgumentException("Não vinheram alterações da tabela: "+ SyncUtil.obterTableNameFromEntity(clazz));

        var createds = change.created;

        repositorioSincronizavel().salvarNovosAtualizarVelhos(createds, clazz);


    }

    protected abstract RepositorioEntidadeSincronizavel repositorioSincronizavel();


}
