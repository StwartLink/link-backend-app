package br.com.linkagrotech.visita_service.servico;

import br.com.linkagrotech.visita_service.repository.VisitaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitaServico {

    @Autowired
    VisitaRepositorio visitaRepositorio;

}
