package br.com.linkagrotech.visita_service.controller;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.repository.VisitaRepositorio;
import br.com.linkagrotech.visita_service.servico.VisitaServico;
import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import jakarta.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/")
public class VisitaController {

    @Autowired
    VisitaServico visitaServico;

    @GetMapping("/puxar")
    public ResponseEntity<List<Visita>> pullNovasVisitas(){

        return ResponseEntity.ok(visitaServico.obterNovos(new Date().getTime()));

    }

    record PullOperacao(Long lastPulledAt){ }

}