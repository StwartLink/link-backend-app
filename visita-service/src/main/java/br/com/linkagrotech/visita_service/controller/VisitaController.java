package br.com.linkagrotech.visita_service.controller;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.servico.VisitaServico;
import br.com.linkagrotech.visita_service.sync.Changes;
import br.com.linkagrotech.visita_service.sync.PullRequestRecord;
import br.com.linkagrotech.visita_service.sync.PullResponseRecord;
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
    public ResponseEntity<PullResponseRecord> pullNovasVisitas(@RequestBody PullRequestRecord pullRequest){

        PullResponseRecord pullResponseRecord = new PullResponseRecord();

        pullResponseRecord.setTimestamp(now());

        pullResponseRecord.setChanges(new Changes(Map.of(Visita.TABLE_NAME,visitaServico.pull(pullRequest))));

        return ResponseEntity.ok(pullResponseRecord);
    }

    private long now(){
        return new Date().getTime();
    }


}
