package br.com.linkagrotech.visita_service.controller;

import br.com.linkagrotech.visita_service.model.Cliente;
import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.servico.VisitaServico;
import br.com.linkagrotech.visita_service.sync.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/")
public class VisitaController {

    @Autowired
    VisitaServico visitaServico;

    @PostMapping("/pull")
    public ResponseEntity<ChangesWrapper> pullNovasVisitas(@RequestBody PullRequestObject pullRequest){

        ChangesWrapper changesWrapper = new ChangesWrapper();

        changesWrapper.setTimestamp(now());

        if(pullRequest.getShcemaVersion()!=null)
            visitaServico.verifySchemaCompatibility(pullRequest.getShcemaVersion());

        changesWrapper.setChanges(Changes.of(visitaServico.pull(pullRequest)));

        return ResponseEntity.ok(changesWrapper);
    }

    @PostMapping("/push")
    public ResponseEntity<String> pushNovasVisitas(@RequestBody ChangesWrapper changesWrapper){

        Changes<?> changes = changesWrapper.getChanges();

        return ResponseEntity.noContent().build();
    }


    private long now(){
        return new Date().getTime();
    }


}
