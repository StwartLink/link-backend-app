package br.com.linkagrotech.visita_service.sync.repositorio;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavel;
import br.com.linkagrotech.visita_service.sync.EntidadeSincronizavelIdentificadorDTO;
import br.com.linkagrotech.visita_service.sync.util.SyncUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;


import java.util.Date;
import java.util.List;

public class RepositorioEntidadeSincronizavelImpl implements RepositorioEntidadeSincronizavel{

    @PersistenceContext
    EntityManager entityManager;

    public EntidadeSincronizavel findById(Long id, Class<? extends EntidadeSincronizavel> entidade){

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

        var query = entityManager.createQuery("select e.id from " + tableName + " e "
                + "where e.deletedAt > :lastPulledAt"
        );

        query.setParameter("lastPulledAt",new Date(lastPulledAt));

        List<?> resultList = query.getResultList();

       return resultList.stream().map(String::valueOf).toList();
    }

    @Override
    @Transactional
    public void salvarNovosAtualizarVelhos(List<EntidadeSincronizavel> sincronizaveis, Class< ? extends  EntidadeSincronizavel> clasz) {
        String tableName = SyncUtil.obterTableNameFromEntity(clasz);

        List<Long> ids = sincronizaveis.stream().map(entidadeSincronizavel -> entidadeSincronizavel.getId()).toList();

        List<String> dispositivos = sincronizaveis.stream().map(entidadeSincronizavel -> entidadeSincronizavel.getDispositivo()).toList();

        //definição de alguém já estar dentro:
        // mesmo id (mobile) e mesmo dispositivo.
        //quero: todos os registros que têm algum par id<->dispositivo, o qual id pertença à [...] e dispositivo à [...]
        // além disso, preciso que cada linha tenha pares válidos

        TypedQuery<EntidadeSincronizavelIdentificadorDTO> query = entityManager.createQuery(
                "select new br.com.linkagrotech.visita_service.sync.EntidadeSincronizavelIdentificadorDTO(e.id, e.dispositivo, e.syncId) from "
                        +tableName+
                        " e where e.id in :ids and e.dispositivo in :dispositivos",
                EntidadeSincronizavelIdentificadorDTO.class
        );

        query.setParameter("ids",ids);

        query.setParameter("dispositivos",dispositivos);

        List<EntidadeSincronizavelIdentificadorDTO> entidadesJaExistentes = query.getResultList();

        sincronizaveis.forEach(newEntity -> {
            var projecoes = entidadesJaExistentes.stream().filter(dto -> dto.match(newEntity.getId(),newEntity.getDispositivo())).toList();
            if (projecoes.isEmpty()) {
                // nao existe projeção no banco, ou seja, a entidade realmente não foi persistida
                newEntity.setCreatedAt(new Date());
                entityManager.persist(newEntity);
            } else {
                // existe projeção da entidade a ser criada no banco, ou seja, ela já foi persistida, e o cliente tenta refazer
                // a operação
                var projecao = projecoes.get(0);
                newEntity.setCreatedAt(new Date());
                newEntity.setUpdatedAt(new Date());
                newEntity.setSyncId(projecao.syncId);
                entityManager.merge(newEntity);
            }

        });
    }
}
