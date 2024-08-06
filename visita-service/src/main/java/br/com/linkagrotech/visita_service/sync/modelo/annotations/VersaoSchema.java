package br.com.linkagrotech.visita_service.sync.modelo.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VersaoSchema {

    long versao();

}
