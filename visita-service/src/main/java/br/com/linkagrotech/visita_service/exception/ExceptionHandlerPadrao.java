package br.com.linkagrotech.visita_service.exception;

import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoExceptionDTO;
import br.com.linkagrotech.visita_service.sync.exception.TipoSincronizacaoErro;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerPadrao {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExcecaoDTO> handleException(Exception e){

        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExcecaoDTO(e));
    }

    @ExceptionHandler(InvalidTypeIdException.class)
    public ResponseEntity<SincronizacaoExceptionDTO> handleFaltaDoTypeExceptions(InvalidTypeIdException ex) {

       return ResponseEntity.internalServerError().body( new SincronizacaoExceptionDTO(TipoSincronizacaoErro.CAMPO_TYPE_FALTANDO));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExcecaoDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExcecaoDTO(
                errorMessage,
                ex.getClass().getSimpleName()
        ));
    }
}
