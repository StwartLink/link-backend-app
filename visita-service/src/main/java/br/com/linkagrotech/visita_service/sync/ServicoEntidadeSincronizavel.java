package br.com.linkagrotech.visita_service.sync;



import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    public Change<T> pull(PullRequestRecord pullRequest) {

        Change<T> change = new Change<>();

        if(pullRequest.lastPulledAt==null || pullRequest.lastPulledAt==0){
            change.created = this.repositorioEntidadeSincronizavel.findAll();
            change.updated = Collections.emptyList();
            change.deleted = Collections.emptyList();
            return change;
        }

        change.created = this.obterCreatedSince(pullRequest.lastPulledAt);
        change.updated = this.obterUpdatedSince(pullRequest.lastPulledAt);
        change.deleted = this.obterDeletedSince(pullRequest.lastPulledAt);

        return  change;
    }

    private List<String> obterDeletedSince(Long lastPulledAt) {
        return repositorioEntidadeSincronizavel.findIdByUpdated_At(new Date(lastPulledAt)).stream().map(String::valueOf).toList();
    }


}
