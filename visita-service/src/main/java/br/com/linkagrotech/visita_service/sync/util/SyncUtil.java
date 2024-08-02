package br.com.linkagrotech.visita_service.sync.util;

import jakarta.persistence.Entity;

public class SyncUtil {
    public static String obterTableNameFromEntity(Class<?> entidade) {
        Entity annotation = entidade.getAnnotation(Entity.class);

        if(annotation==null)
            throw new RuntimeException("Classe passada não é uma entidade");

        String tableName = annotation.name();

        if(tableName==null || tableName==""){
            tableName = entidade.getSimpleName();
        }
        return tableName;
    }
}
