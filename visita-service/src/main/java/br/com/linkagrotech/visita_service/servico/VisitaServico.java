package br.com.linkagrotech.visita_service.servico;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.repository.VisitaRepositorio;
import br.com.linkagrotech.visita_service.sync.ServicoEntidadeSincronizavel;
import org.springframework.stereotype.Service;

@Service
public class VisitaServico extends ServicoEntidadeSincronizavel<Visita,Long> {

    VisitaRepositorio visitaRepositorio;

    public VisitaServico(VisitaRepositorio visitaRepositorio) {
        super(visitaRepositorio);
        this.visitaRepositorio = visitaRepositorio;
    }


}
