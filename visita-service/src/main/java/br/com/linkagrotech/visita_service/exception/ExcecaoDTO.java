package br.com.linkagrotech.visita_service.exception;

import lombok.Data;
import lombok.Getter;

@Data
public class ExcecaoDTO {


    String mensagem;

    String causa;
    public ExcecaoDTO(Exception e){
        this.mensagem = e.getMessage();
        this.causa = e.getCause()!=null? e.getCause().toString() : e.getClass().getSimpleName();
    }
    public ExcecaoDTO(String mensagem, String causa){
        this.mensagem = mensagem;
        this.causa = causa;
    }

}
