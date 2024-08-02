package br.com.linkagrotech.visita_service.sync;



import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.repositorio.RepositorioEntidadeSincronizavel;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class ServicoEntidadeSincronizavel<T extends EntidadeSincronizavel, ID extends Serializable> {

    RepositorioEntidadeSincronizavel<T,ID> repositorioEntidadeSincronizavel;

    public ServicoEntidadeSincronizavel(RepositorioEntidadeSincronizavel<T,ID>  repositorioEntidadeSincronizavel){

        this.repositorioEntidadeSincronizavel = repositorioEntidadeSincronizavel;

    }

    private List<T> obterCreatedSince(Long lastPulledAt){
        return repositorioEntidadeSincronizavel.findByCreatedAtGreaterThan(new Date(lastPulledAt));
    }

    private List<T> obterUpdatedSince(Long lastPulledAt) {
        return repositorioEntidadeSincronizavel.findByUpdatedAtGreaterThan(new Date(lastPulledAt));
    }

    private List<String> obterDeletedSince(Long lastPulledAt) {
        return repositorioEntidadeSincronizavel.findByDeletedAtGreaterThan(new Date(lastPulledAt)).stream().map(t->
                String.valueOf(t.getId())).toList();
    }

    public Map.Entry<String,Change<T>> pull(PullRequestObject pullRequest) {

        Change<T> change = new Change<>();

        if(pullRequest.lastPulledAt==null || pullRequest.lastPulledAt==0){
            change.created = this.repositorioEntidadeSincronizavel.findAll();
            change.updated = Collections.emptyList();
            change.deleted = Collections.emptyList();
            return Map.entry(getTableName(),change);
        }

        change.created = this.obterCreatedSince(pullRequest.lastPulledAt);
        change.updated = this.obterUpdatedSince(pullRequest.lastPulledAt);
        change.deleted = this.obterDeletedSince(pullRequest.lastPulledAt);

        return  Map.entry(getTableName(),change);
    }

    public void verifySchemaCompatibility(Long clientSchema) throws RuntimeException {

        if(Visita.SCHEMA_VERSION > clientSchema){
            throw new RuntimeException("A versão do schema do cliente é anterior à versão do servidor!");
        }

    }

    public void push(Changes<T> changes) {


    }

    private String getTableName() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType parameterizedType) {

            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            Type entityType = typeArguments[0];

            if (entityType instanceof Class<?> entityClass && (entityClass.isAnnotationPresent(Entity.class))) {

                    Entity entityAnnotation = entityClass.getAnnotation(Entity.class);

                    String entityName = entityAnnotation.name();

                    if (entityName.isEmpty()) {
                        entityName = entityClass.getSimpleName();
                    }

                    return entityName;

            }

        }

        return "entity"; // Retorne entity se não conseguir obter o nome da entidade
    }

}
