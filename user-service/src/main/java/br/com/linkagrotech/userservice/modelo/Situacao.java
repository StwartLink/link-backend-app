package br.com.linkagrotech.userservice.modelo;

public enum Situacao {

    ATIVO("ATIVO"),
    REQUISITADO("REQUISITADO");


    private String valor;

    Situacao(String valor){
        this.valor = valor;
    }

}
