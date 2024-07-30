package br.com.linkagrotech.visita_service.sync;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoRepositoryBean
public interface RepositorioEntidadeSincronizavel<T extends EntidadeSincronizavel, ID extends Serializable> extends JpaRepository<T,ID> {

    List<T> findByCreatedAtGreaterThan(Date lastPulledAt);


}
