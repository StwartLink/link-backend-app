package br.com.linkagrotech.visita_service.repository;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.repositorio.RepositorioEntidadeSincronizavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitaRepositorio extends JpaRepository<Visita,Long> {

}
