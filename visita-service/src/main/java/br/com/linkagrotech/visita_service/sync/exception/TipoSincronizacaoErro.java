package br.com.linkagrotech.visita_service.sync.exception;

import lombok.Getter;

@Getter
public enum TipoSincronizacaoErro {

    DESCONHECIDO(0,"Ocorreu um erro desconhecido em umas das operações de sincronização"),
    UPDATE_INVALIDO_ID_INEXISTENTE(1,"Não foi possível realizar a operação de update, pois a(s) entidade(s) (syncId) não existe(em) no banco de dados"),
    UPDATE_INVALIDO_ID_SOFT_DELETADO(2,"Não foi possível realizar a operação de update, pois a(s) entidade(s) já foram deletadas previamente por meio de soft delete"),
    PUSH_INVALIDO_SEM_ALTERACOES(3,"Não foram encontradas alterações para realizar o push");


    private int codigo;
    private String descricao;

    TipoSincronizacaoErro(int codigo,String descricao){
        this.codigo = codigo;
        this.descricao = descricao;
    }

}
