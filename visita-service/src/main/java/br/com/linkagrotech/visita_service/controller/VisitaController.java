package br.com.linkagrotech.visita_service.controller;

import br.com.linkagrotech.visita_service.model.TipoVisita;
import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.repository.ClienteRepositorio;
import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoException;
import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoExceptionDTO;
import br.com.linkagrotech.visita_service.sync.modelo.Changes;
import br.com.linkagrotech.visita_service.sync.modelo.ChangesWrapper;
import br.com.linkagrotech.visita_service.sync.modelo.PullRequestWrapper;
import br.com.linkagrotech.visita_service.sync.servico.ServicoEntidadeSincronizavel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/")
public class VisitaController {

    @Autowired
    ServicoEntidadeSincronizavel servicoSincronizavel;

    @Autowired
    ClienteRepositorio clienteRepositorio;

    @PostMapping("/pull")
    public ResponseEntity<ChangesWrapper> pullNovasVisitas(@RequestBody PullRequestWrapper pullRequest){

        ChangesWrapper changesWrapper = new ChangesWrapper();

        changesWrapper.setTimestamp(now());

        changesWrapper.setChanges(servicoSincronizavel.pullEntities(pullRequest, Visita.class, TipoVisita.class));

        return ResponseEntity.ok(changesWrapper);
    }

    @PostMapping("/push")
    public ResponseEntity pushNovasVisitas(@RequestBody ChangesWrapper changesWrapper)  {

        Changes changes = changesWrapper.getChanges();

        try {
            servicoSincronizavel.pushEntities(changes, Visita.class, TipoVisita.class);
        } catch (SincronizacaoException e) {
            return ResponseEntity.internalServerError().body(new SincronizacaoExceptionDTO(e));
        }

        return ResponseEntity.noContent().build();
    }



    private long now(){
        return new Date().getTime();
    }


}
