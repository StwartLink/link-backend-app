package br.com.linkagrotech.visita_service.sync.exception;

import lombok.Getter;

@Getter
public enum TipoSincronizacaoErro {

    DESCONHECIDO(0,"Ocorreu um erro desconhecido em umas das operações de sincronização"),
    UPDATE_INVALIDO_ID_INEXISTENTE(1,"Não foi possível realizar a operação de update, pois a(s) entidade(s) não existe(em) no banco de dados"),
    UPDATE_INVALIDO_ID_SOFT_DELETADO(2,"Não foi possível realizar a operação de update, pois a(s) entidade(s) já foram deletadas previamente por meio de soft delete"),
    PUSH_INVALIDO_SEM_ALTERACOES(3,"Não foram encontradas alterações para realizar o push"),
    UPDATE_CONFLITUOSO(4,"Possível conflito: Requisição de update em entidade antiga, faça o pull para obter o estado mais recente do servidor, faça as alterações e então sincronize os updates"),
    DELETE_CONFLITUOSO(5, "Possível conflito: Requisição de delete em entidade antiga, faça o pull para obter o estado mais recente do servidor, faça as alterações e então sincronize as deleções"),
    VERSAO_SCHEMA_DIFERENTE(6,"Operação inválida: a sua versão de schema é diferente da versão do servidor. Por favor, atualize o aplicativo para obter a versão mais recente"),
    CAMPO_TYPE_FALTANDO(8,"Campo '_type' faltando ou inválido em algum dos JSONs de entidade.");


    private int codigo;
    private String descricao;

    TipoSincronizacaoErro(int codigo,String descricao){
        this.codigo = codigo;
        this.descricao = descricao;
    }

}
