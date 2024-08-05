package br.com.linkagrotech.visita_service.sync.repositorio;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoException;
import br.com.linkagrotech.visita_service.sync.exception.TipoSincronizacaoErro;
import br.com.linkagrotech.visita_service.sync.util.SyncUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public class RepositorioEntidadeSincronizavelImpl implements RepositorioEntidadeSincronizavel{

    @PersistenceContext
    EntityManager entityManager;

    private EntidadeSincronizavel findBySyncId(Long id, Class<? extends EntidadeSincronizavel> entidade){

        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e from " + tableName + " e "
                + "where e.syncId > :id",entidade
        );

        query.setParameter("id",id);

        return query.getSingleResult();
    }

    public List<EntidadeSincronizavel> obterTodos(Class<? extends EntidadeSincronizavel> entidade) {

        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e from " + tableName + " e");

        return query.getResultList();
    }

    @Override
    public List<EntidadeSincronizavel> obterCreatedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade) {

        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e from " + tableName + " e "
        + "where e.createdAt > :lastPulledAt"
        );

        query.setParameter("lastPulledAt",new Date(lastPulledAt));

        return query.getResultList();
    }

    @Override
    public List<EntidadeSincronizavel> obterUpdatedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade) {
        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e from " + tableName + " e "
                + "where e.updatedAt > :lastPulledAt"
        );

        query.setParameter("lastPulledAt",new Date(lastPulledAt));

        return query.getResultList();
    }

    @Override
    public List<String> obterDeletedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade) {

        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e.syncId from " + tableName + " e "
                + "where e.deletedAt > :lastPulledAt"
        );

        query.setParameter("lastPulledAt",new Date(lastPulledAt));

        List<?> resultList = query.getResultList();

       return resultList.stream().map(String::valueOf).toList();
    }

    @Override
    @Transactional
    public void salvarSincronizaveis(List<EntidadeSincronizavel> sincronizaveis, Class< ? extends  EntidadeSincronizavel> clazz) {
        String tableName = SyncUtil.obterTableNameFromEntity(clazz);

        List<Long> ids = sincronizaveis.stream().map(EntidadeSincronizavel::getId).toList();

        List<String> dispositivos = sincronizaveis.stream().map(EntidadeSincronizavel::getDispositivo).toList();

        //definição de alguém já estar dentro:
        // mesmo id (mobile) e mesmo dispositivo.
        //quero: todos os registros que têm algum par id<->dispositivo, o qual id pertença à [...] e dispositivo à [...]
        // além disso, preciso que cada linha tenha pares válidos

        TypedQuery<EntidadeSincronizavel> query = entityManager.createQuery(
                "select e from "
                        +tableName+
                        " e where e.id in :ids and e.dispositivo in :dispositivos",
                EntidadeSincronizavel.class
        );

        query.setParameter("ids",ids);

        query.setParameter("dispositivos",dispositivos);

        List<EntidadeSincronizavel> entidadesJaExistentes = query.getResultList();

        sincronizaveis.forEach(newEntity -> {

            var projecoes = entidadesJaExistentes.stream().filter(dto -> match(newEntity.getId(),newEntity.getDispositivo() , dto)).toList();
            if (projecoes.isEmpty()) {
                // nao existe projeção no banco, ou seja, a entidade realmente não foi persistida
                entityManager.persist(newEntity);
            } else {
                // existe projeção da entidade a ser criada no banco, ou seja, ela já foi persistida, e o cliente tenta refazer
                // a operação
                var projecao = projecoes.get(0);

                newEntity.setCreatedAt(projecao.getCreatedAt());

                newEntity.setSyncId(projecao.getSyncId());

                entityManager.merge(newEntity);
            }
        });
    }

    /**
     * Atualiza a lista de entidades sincronizáveis.
     * É esperado que elas possuam já um syncId, visto que já foram criadas.
     *
     * @param updateds
     * @param clazz
     */
    @Override
    @Transactional
    public void atualizarSincronizaveis(List<EntidadeSincronizavel> updateds, Class<? extends EntidadeSincronizavel> clazz) throws SincronizacaoException {

        String tableName = SyncUtil.obterTableNameFromEntity(clazz);

        /*
        ID que veio para atualizar não existe no banco:
        1: A entidade de ID a atualizar foi deletada e não pode mais ser atualizada --> Exception
        2: Só existirá soft delete no sistema é, então ela deve ser criada para manter a uniformidade na sincronização
        */

        List<Long> syncIdsParaAtualizar = updateds.stream().map(EntidadeSincronizavel::getSyncId).toList();

        TypedQuery<EntidadeSincronizavel> query = entityManager.createQuery(
                "select e from " +tableName+ " e where e.syncId in :syncIds",
                EntidadeSincronizavel.class
        );

        query.setParameter("syncIds",syncIdsParaAtualizar);

        List<EntidadeSincronizavel> entidadesNoBanco = query.getResultList();

        List<Long> syncIdsNoBanco = entidadesNoBanco.stream().map(EntidadeSincronizavel::getSyncId).toList();

        List<Long> syncIdsFantasmas =  syncIdsParaAtualizar.stream().filter(idAtualizar->!syncIdsNoBanco.contains(idAtualizar)).toList();

        List<EntidadeSincronizavel> syncEntidadesSoftDeletados =  entidadesNoBanco.stream().filter(e-> e.getDeletedAt()!=null).toList();

        if(!syncIdsFantasmas.isEmpty()){
            throw new SincronizacaoException(TipoSincronizacaoErro.UPDATE_INVALIDO_ID_INEXISTENTE, updateds.stream().filter(u->syncIdsFantasmas.contains(u.getSyncId())).toList());
        }

        if(!syncEntidadesSoftDeletados.isEmpty()){
            throw new SincronizacaoException( TipoSincronizacaoErro.UPDATE_INVALIDO_ID_SOFT_DELETADO, syncEntidadesSoftDeletados);
        }

        for (EntidadeSincronizavel entidadeSincronizavel : updateds) {
            entityManager.merge(entidadeSincronizavel);
        }

    }

    @Override
    @Transactional
    public void deletarSincronizaveis(List<String> deleted, Class<? extends EntidadeSincronizavel> clazz) {

        String tableName = SyncUtil.obterTableNameFromEntity(clazz);

        Query query = entityManager.createQuery("update "+tableName + " e set e.deletedAt = :deletedAt where " +
                "e.syncId in :syncIdList and e.deletedAt IS NULL");

        query.setParameter("deletedAt", new Date());

        query.setParameter("syncIdList",deleted.stream().map(Long::valueOf).toList());

        query.executeUpdate();
    }

    public boolean match(Long id, String dispositivo, EntidadeSincronizavel entidadeSincronizavel){
        return id.equals(entidadeSincronizavel.getId()) && dispositivo.equals(entidadeSincronizavel.getDispositivo());
    }
}
