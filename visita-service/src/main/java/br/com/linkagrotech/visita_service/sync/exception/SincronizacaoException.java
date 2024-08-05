package br.com.linkagrotech.visita_service.sync.exception;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SincronizacaoException extends Exception{

    private TipoSincronizacaoErro tipoSincronizacaoErro;

    private List<EntidadeSincronizavel> rejeitados;

    public SincronizacaoException(String msg){
        super(msg);
    }

    public SincronizacaoException(TipoSincronizacaoErro tipoSincronizacaoErro){
        super(tipoSincronizacaoErro.getDescricao());
        this.tipoSincronizacaoErro = tipoSincronizacaoErro;
    }

    public SincronizacaoException(TipoSincronizacaoErro tipoSincronizacaoErro, String msg){
        super(msg);
        this.tipoSincronizacaoErro = tipoSincronizacaoErro;
    }


    public SincronizacaoException(TipoSincronizacaoErro tipoSincronizacaoErro,List<EntidadeSincronizavel> rejeitados){
        super(tipoSincronizacaoErro.getDescricao());
        this.rejeitados = rejeitados;
        this.tipoSincronizacaoErro = tipoSincronizacaoErro;
    }

}
