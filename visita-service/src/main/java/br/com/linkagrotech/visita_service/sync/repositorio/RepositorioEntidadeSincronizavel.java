package br.com.linkagrotech.visita_service.sync.repositorio;

import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoRepositoryBean
public interface RepositorioEntidadeSincronizavel<T extends EntidadeSincronizavel, ID extends Serializable>
        extends JpaRepository<T,ID>{

    List<T> findByCreatedAtGreaterThan(Date lastPulledAt);

    List<T> findByUpdatedAtGreaterThan(Date lastPulledAt);

    List<T> findByDeletedAtGreaterThan(Date date);


}
