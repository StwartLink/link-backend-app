package br.com.linkagrotech.visita_service.sync.exception;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SincronizacaoExceptionDTO {

    String erro;
    String codigo;
    String descricao;

    List<EntidadeSincronizavel> rejeitados;

    String mensagem;

    public SincronizacaoExceptionDTO(SincronizacaoException e) {
        this.erro = e.getTipoSincronizacaoErro().toString();
        this.codigo = String.valueOf(e.getTipoSincronizacaoErro().getCodigo());
        this.descricao = e.getTipoSincronizacaoErro().getDescricao();
        this.rejeitados = e.getRejeitados();
        this.mensagem  = e.getMessage();
    }
    public SincronizacaoExceptionDTO(TipoSincronizacaoErro tipoSincronizacaoErro){
        this.erro = tipoSincronizacaoErro.toString();
        this.codigo = String.valueOf(tipoSincronizacaoErro.getCodigo());
        this.descricao = tipoSincronizacaoErro.getDescricao();
        this.rejeitados = new ArrayList<>();
        this.mensagem = tipoSincronizacaoErro.getDescricao();

    }
}
