package br.com.linkagrotech.auth_service.dto;

import lombok.Getter;

@Getter
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
