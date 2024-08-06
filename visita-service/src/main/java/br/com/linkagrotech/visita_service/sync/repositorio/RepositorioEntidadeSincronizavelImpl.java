package br.com.linkagrotech.visita_service.sync.repositorio;

import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavelUtils;
import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.exception.SincronizacaoException;
import br.com.linkagrotech.visita_service.sync.exception.TipoSincronizacaoErro;
import br.com.linkagrotech.visita_service.sync.util.SyncUtil;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class RepositorioEntidadeSincronizavelImpl implements RepositorioEntidadeSincronizavel{

    @PersistenceContext
    EntityManager entityManager;

    private EntidadeSincronizavel findById(Long id, Class<? extends EntidadeSincronizavel> entidade){

        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e from " + tableName + " e "
                + "where e.id > :id",entidade
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
        + "where e.createdAt > :lastPulledAt",EntidadeSincronizavel.class
        );

        query.setParameter("lastPulledAt", Instant.ofEpochMilli(lastPulledAt));

        var createdsSince = query.getResultList();

        createdsSince.forEach(EntidadeSincronizavelUtils::preencherValoresTransientes);

        return createdsSince;
    }

    @Override
    public List<EntidadeSincronizavel> obterUpdatedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade) {

        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e from " + tableName + " e " + "where e.updatedAt > :lastPulledAt",
                EntidadeSincronizavel.class);

        query.setParameter("lastPulledAt",Instant.ofEpochMilli(lastPulledAt));

        var createdsSince = query.getResultList();

        createdsSince.forEach(EntidadeSincronizavelUtils::preencherValoresTransientes);

        return createdsSince;
    }

    @Override
    public List<UUID> obterDeletedSince(Long lastPulledAt, Class<? extends EntidadeSincronizavel> entidade) {

        String tableName = SyncUtil.obterTableNameFromEntity(entidade);

        var query = entityManager.createQuery("select e.id from " + tableName + " e "
                + "where e.deletedAt > :lastPulledAt"
        );

        query.setParameter("lastPulledAt",Instant.ofEpochMilli(lastPulledAt));

        List<UUID> resultList = query.getResultList();

       return resultList;
    }

    @Override
    @Transactional
    public void salvarSincronizaveis(List<EntidadeSincronizavel> sincronizaveis, Class< ? extends  EntidadeSincronizavel> clazz) throws SincronizacaoException {

        String tableName = SyncUtil.obterTableNameFromEntity(clazz);

        List<UUID> ids = sincronizaveis.stream().map(EntidadeSincronizavel::getId).toList();

        TypedQuery<EntidadeSincronizavel> query = entityManager.createQuery(
                "select e from "
                        +tableName+
                        " e where e.id in :ids ",
                EntidadeSincronizavel.class
        );

        query.setParameter("ids",ids);

        //entidades que já foram sincronizadas, mas o front/mobile não sabe que foram ainda e por isso ele está requerindo para salvar novamente
        //nesse caso, é realizado um "update"
        List<EntidadeSincronizavel> entidadesJaExistentes = query.getResultList();

        sincronizaveis.forEach(newEntity -> {

            var projecoes = entidadesJaExistentes.stream().filter(dto -> match(newEntity.getId(), dto)).toList();

            if (projecoes.isEmpty()) {
                // nao existe projeção no banco, ou seja, a entidade realmente não foi persistida
                EntidadeSincronizavelUtils.preencherRelacoes(newEntity);
                entityManager.merge(newEntity);
            } else {
                // existe projeção da entidade a ser criada no banco, ou seja, ela já foi persistida, e o cliente tenta refazer
                // a operação
                var projecao = projecoes.get(0);

                newEntity.setCreatedAt(projecao.getCreatedAt());

                EntidadeSincronizavelUtils.preencherRelacoes(newEntity);

                entityManager.merge(newEntity);
            }
        });
    }

    /**
     * Atualiza a lista de entidades sincronizáveis.
     * É esperado que elas possuam já uma entidade no banco, visto que já foram criadas.
     *
     * @param updateds
     * @param clazz
     */
    @Override
    @Transactional
    public void atualizarSincronizaveis(List<EntidadeSincronizavel> updateds, Class<? extends EntidadeSincronizavel> clazz) throws SincronizacaoException {

        String tableName = SyncUtil.obterTableNameFromEntity(clazz);


        List<UUID> idsParaAtualizar = updateds.stream().map(EntidadeSincronizavel::getId).toList();

        TypedQuery<EntidadeSincronizavel> query = entityManager.createQuery(
                "select e from " +tableName+ " e where e.id in :ids",
                EntidadeSincronizavel.class
        );

        query.setParameter("ids",idsParaAtualizar);

        List<EntidadeSincronizavel> entidadeAtualizarNoBanco = query.getResultList();

        List<UUID> idsAtualizarNoBanco = entidadeAtualizarNoBanco.stream().map(EntidadeSincronizavel::getId).toList();

        List<UUID> idsFantasmas =  idsParaAtualizar.stream().filter(idAtualizar->!idsAtualizarNoBanco.contains(idAtualizar)).toList();

        List<EntidadeSincronizavel> entidadesSoftDeletadas =  entidadeAtualizarNoBanco.stream().filter(e-> e.getDeletedAt()!=null).toList();

        if(!idsFantasmas.isEmpty()){
            throw new SincronizacaoException(TipoSincronizacaoErro.UPDATE_INVALIDO_ID_INEXISTENTE, updateds.stream().filter(u->idsFantasmas.contains(u.getId())).toList());
        }

        if(!entidadesSoftDeletadas.isEmpty()){
            throw new SincronizacaoException( TipoSincronizacaoErro.UPDATE_INVALIDO_ID_SOFT_DELETADO, entidadesSoftDeletadas);
        }


        for (EntidadeSincronizavel entidadeSincronizavel : updateds) {

            EntidadeSincronizavelUtils.preencherRelacoes(entidadeSincronizavel);

            entityManager.merge(entidadeSincronizavel);
        }

    }

    @Override
    @Transactional
    public void deletarSincronizaveis(List<UUID> deleted, Class<? extends EntidadeSincronizavel> clazz) {

        String tableName = SyncUtil.obterTableNameFromEntity(clazz);

        Query query = entityManager.createQuery("update "+tableName + " e set e.deletedAt = :deletedAt where " +
                "e.id in :idList and e.deletedAt IS NULL");

        query.setParameter("deletedAt", Instant.now());

        query.setParameter("idList",deleted);

        query.executeUpdate();
    }

    @Override
    public void verificarConflitoUpdate(List<EntidadeSincronizavel> updates, Long lastPulledAt, Class<? extends EntidadeSincronizavel> clazz) throws SincronizacaoException {

        var query = getConflituosoQuery(updates.stream().map(EntidadeSincronizavel::getId).toList(),lastPulledAt,clazz);

        var conflituososComListaUpdates = query.getResultList();

        if(!conflituososComListaUpdates.isEmpty()){
            throw new SincronizacaoException(TipoSincronizacaoErro.UPDATE_CONFLITUOSO, conflituososComListaUpdates);
        }

    }



    @Override
    public void verificarConflitoDeletar(List<UUID> deleted, Long lastPulledAt, Class<? extends EntidadeSincronizavel> clazz) throws SincronizacaoException {

        var query = getConflituosoQuery(deleted,lastPulledAt,clazz);

        var conflituososComListaUpdates = query.getResultList();

        if(!conflituososComListaUpdates.isEmpty()){
            throw new SincronizacaoException(TipoSincronizacaoErro.DELETE_CONFLITUOSO, conflituososComListaUpdates);
        }

    }


    private boolean match(UUID id, EntidadeSincronizavel entidadeSincronizavel){
        return id.equals(entidadeSincronizavel.getId());
    }

    private  TypedQuery<EntidadeSincronizavel> getConflituosoQuery(List<UUID> idList, Long lastPulledAt, Class<? extends EntidadeSincronizavel> clazz ){
        String tableName = SyncUtil.obterTableNameFromEntity(clazz);
        TypedQuery<EntidadeSincronizavel> query = entityManager.createQuery("select e from " + tableName + " e where e.updatedAt> :lastPulledAt and e.id in :idList",EntidadeSincronizavel.class);
        query.setParameter("lastPulledAt",Instant.ofEpochMilli(lastPulledAt));
        query.setParameter("idList",idList);
        return query;
    }
}
