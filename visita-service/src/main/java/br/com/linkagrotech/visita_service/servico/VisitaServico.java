package br.com.linkagrotech.visita_service.servico;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.RepositorioEntidadeSincronizavel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VisitaServico{

    @Autowired
    RepositorioEntidadeSincronizavel<Visita,Long> repositorioEntidadeSincronizavel;

    public List<Visita> obterNovos(Long lastPulledAt){
        return repositorioEntidadeSincronizavel.findByCreatedAtGreaterThan(new Date(lastPulledAt));
    }

}
