package br.com.linkagrotech.visita_service.model;

public enum TipoRecorrencia {

    ESPECIFICA("ESPECIFICA"),
    DIARIA("DIARIA"),
    MENSAL("MENSAL"),
    ANUAL("ANUAL");

    String valor;

    TipoRecorrencia(String valor){
        this.valor = valor;
    }

}
