package br.com.linkagrotech.userservice.config;

import br.com.linkagrotech.userservice.dto.ExcecaoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerPadrao {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExcecaoDTO> handleException(Exception e){
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExcecaoDTO(e));
    }
}
