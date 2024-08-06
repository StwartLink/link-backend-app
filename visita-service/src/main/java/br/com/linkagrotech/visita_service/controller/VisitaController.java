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
import br.com.linkagrotech.visita_service.sync.util.SyncUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/")
public class VisitaController {

    @Autowired
    ServicoEntidadeSincronizavel servicoSincronizavel;

    @Autowired
    ClienteRepositorio clienteRepositorio;

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Autowired
    @Qualifier("sincronizacaoObjectMapper")
    private ObjectMapper sincronizacaoObjectMapper;

    @PostConstruct
    public void init() {
        // Configure the MappingJackson2HttpMessageConverter with the custom ObjectMapper
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(sincronizacaoObjectMapper);
        requestMappingHandlerAdapter.setMessageConverters(Collections.singletonList(converter));
    }

    @PostMapping("/pull")
    public ResponseEntity<?> pullNovasVisitas(@RequestBody PullRequestWrapper pullRequest){

        ChangesWrapper changesWrapper = new ChangesWrapper();

        changesWrapper.setTimestamp(now());

        try {
            changesWrapper.setChanges(servicoSincronizavel.pullEntities(pullRequest, SyncUtil.obterClassesSincronizaveis().stream().toList()));
        } catch (SincronizacaoException e) {
            return ResponseEntity.internalServerError().body(new SincronizacaoExceptionDTO(e));
        }

        return ResponseEntity.ok(changesWrapper);
    }

    @PostMapping("/push")
    public ResponseEntity<?> pushNovasVisitas(@RequestBody ChangesWrapper changesWrapper)  {

        try {
            servicoSincronizavel.pushEntities(changesWrapper, SyncUtil.obterClassesSincronizaveis().stream().toList());
        } catch (SincronizacaoException e) {
            return ResponseEntity.internalServerError().body(new SincronizacaoExceptionDTO(e));
        }

        return ResponseEntity.noContent().build();
    }



    private long now(){
        return new Date().getTime();
    }


}
