package br.com.linkagrotech.visita_service.sync;

import java.util.List;

public class Migration {

    /* *******************

    Escolher a estratégia from-schemaVersion permite saber se o dispositivo requerinte cresceu o banco dele, porém somente isso.

     ******************* */

    /**
     * Versão do schema do aplicativo pega na última sincronização
     */
    Long from;

    /* *******************

    Utilizando a estragétia tables-columns, deve-se limitar os valores os quais os dispositivos requerintes podem acessar em uma
    whitelist (tipagem de queries, validações, etc...).

    Com essa estratégia, é sabido exatamente o que mudou no aplicativo, e assim é possível tratar caso a caso automaticamente,
    por isso a preocupação com a segurança acima.

     ******************* */

    /**
     * Novas tabelas que foram adicionadas no aplicativo desde a última sincronização
     */
    List<String> tables;


    /**
     * Lista de objetos "Columns", que contém o par "tabela/lista de colunas alteradas da tabela"
     */
    List<Columns> columns;

}
