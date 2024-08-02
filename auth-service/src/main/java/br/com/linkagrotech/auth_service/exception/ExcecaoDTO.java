package br.com.linkagrotech.auth_service.exception;

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

    public static String stringInstance(Exception e){

        String str = "";
        try{
            str = new ObjectMapper().writeValueAsString(new ExcecaoDTO(e));
            return str;
        } catch (JsonProcessingException ex) {
            return "{}";
        }

    }
    public static String stringInstance(String mensagem, String causa){

        String str = "";
        try{
            str = new ObjectMapper().writeValueAsString(new ExcecaoDTO(mensagem,causa));
            return str;
        } catch (JsonProcessingException ex) {
            return "{}";
        }

    }

}
