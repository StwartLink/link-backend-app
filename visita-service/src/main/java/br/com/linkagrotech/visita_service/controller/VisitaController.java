package br.com.linkagrotech.visita_service.controller;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.repository.ClienteRepositorio;
import br.com.linkagrotech.visita_service.servico.VisitaServico;
import br.com.linkagrotech.visita_service.sync.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/")
public class VisitaController {

    @Autowired
    VisitaServico visitaServico;

    @Autowired
    ClienteRepositorio clienteRepositorio;

    @PostMapping("/pull")
    public ResponseEntity<ChangesWrapper> pullNovasVisitas(@RequestBody PullRequestObject pullRequest){

        ChangesWrapper changesWrapper = new ChangesWrapper();

        changesWrapper.timestamp = now();

        if(pullRequest.getShcemaVersion()!=null)
            visitaServico.verifySchemaCompatibility(pullRequest.getShcemaVersion());

        changesWrapper.changes= Changes.of(visitaServico.pullEntities(pullRequest, Visita.class));

        return ResponseEntity.ok(changesWrapper);
    }

    @PostMapping("/push")
    public ResponseEntity<String> pushNovasVisitas(@RequestBody ChangesWrapper changesWrapper){

        clienteRepositorio.obterPorFoo();

        Changes changes = changesWrapper.changes;

        visitaServico.pushEntities(changes, Visita.class);

        return ResponseEntity.noContent().build();
    }



    private long now(){
        return new Date().getTime();
    }


}
