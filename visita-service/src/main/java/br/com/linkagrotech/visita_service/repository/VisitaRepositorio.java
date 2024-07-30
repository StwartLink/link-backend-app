package br.com.linkagrotech.visita_service.repository;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.RepositorioEntidadeSincronizavel;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitaRepositorio extends RepositorioEntidadeSincronizavel<Visita,Long>{
}
