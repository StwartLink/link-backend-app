package br.com.linkagrotech.visita_service.sync.util;

import br.com.linkagrotech.visita_service.sync.modelo.annotations.VersaoSchema;
import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import jakarta.persistence.Entity;
import org.reflections.Reflections;

import java.util.Set;

public class SyncUtil {

    private SyncUtil(){}

    public static String obterTableNameFromEntity(Class<?> entidade) {
        Entity annotation = entidade.getAnnotation(Entity.class);

        if(annotation==null)
            throw new RuntimeException("Classe passada não é uma entidade");

        String tableName = annotation.name();

        if(tableName==null || "".equals(tableName)){
            tableName = entidade.getSimpleName();
        }
        return tableName;
    }

    public static Set<Class<? extends EntidadeSincronizavel>> obterClassesSincronizaveis(){
        Reflections reflections = new Reflections("br.com.linkagrotech");
        return reflections.getSubTypesOf(EntidadeSincronizavel.class);
    }

    public static long obterSchemaVersion(Class<? extends EntidadeSincronizavel> clazz) {

        if (clazz.isAnnotationPresent(VersaoSchema.class)) {
            VersaoSchema schemaVersion = clazz.getAnnotation(VersaoSchema.class);
            return schemaVersion.versao();
        }

        return 0L;

    }
}
