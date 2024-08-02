package br.com.linkagrotech.visita_service.servico;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.repository.VisitaRepositorio;
import br.com.linkagrotech.visita_service.sync.ServicoEntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.repositorio.RepositorioEntidadeSincronizavel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitaServico extends ServicoEntidadeSincronizavel {

    @Autowired
    VisitaRepositorio visitaRepositorio;


    @Override
    protected RepositorioEntidadeSincronizavel repositorioSincronizavel() {
        return this.visitaRepositorio;
    }
}
