package br.com.linkagrotech.visita_service.sync.modelo;

import lombok.Data;

import java.util.List;

@Data
public class Columns {
    /**
     * Tabela da coluna
     */
    private String table;


    /**
     * Colunas da tabela
     */
    private List<String> columns;

}
