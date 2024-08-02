package br.com.linkagrotech.userservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
