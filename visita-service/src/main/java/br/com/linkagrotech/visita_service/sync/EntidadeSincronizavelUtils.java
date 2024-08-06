package br.com.linkagrotech.visita_service.sync;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;

public class EntidadeSincronizavelUtils {

    private EntidadeSincronizavelUtils(){}

    public static void preencherValoresTransientes(EntidadeSincronizavel entity) {
        try {
            // Obtém a classe da entidade
            Class<?> clazz = entity.getClass();

            // Itera sobre todos os métodos da classe
            for (Method method : clazz.getDeclaredMethods()) {
                if (isGetter(method)) {
                    // Obtém o nome do campo correspondente ao getter
                    String fieldName = getFieldNameFromGetter(method);
                    Field field = clazz.getDeclaredField(fieldName);

                    // Verifica se o campo tem a anotação @JoinColumn
                    JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
                    if (joinColumnAnnotation != null) {
                        // Invoca o método getter para obter a entidade associada
                        Object associatedEntity = method.invoke(entity);

                        if (associatedEntity != null) {
                            // Obtém o ID da entidade associada
                            Method getIdMethod = associatedEntity.getClass().getMethod("getId");
                            UUID associatedEntityId = (UUID) getIdMethod.invoke(associatedEntity);

                            // Constrói o nome do setter transiente correspondente
                            String transientFieldName = joinColumnAnnotation.name() ;
                            String setterName = "set" + transientFieldName.substring(0, 1).toUpperCase() + transientFieldName.substring(1);

                            // Obtém o método setter
                            Method setterMethod = clazz.getMethod(setterName, UUID.class);

                            // Invoca o setter para preencher o campo transiente com o ID
                            setterMethod.invoke(entity, associatedEntityId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void preencherRelacoes(EntidadeSincronizavel entity) {
        // Obtém a classe da entidade
        Class<?> clazz = entity.getClass();

        // Itera sobre todos os métodos da classe
        for (Method method : clazz.getDeclaredMethods()) {
            // Verifica se o método é um setter de entidade JPA
            if (isJpaEntitySetter(method)) {
                try {
                    // Obtém o nome do campo correspondente ao setter
                    String fieldName = getFieldNameFromSetter(method);
                    Field field = clazz.getDeclaredField(fieldName);

                    // Verifica se o campo tem a anotação @JoinColumn
                    JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
                    if (joinColumnAnnotation != null) {
                        // Obtém o nome da coluna do @JoinColumn
                        String joinColumn = joinColumnAnnotation.name();

                        // Constrói o nome do método getter correspondente
                        String getterName = "get" + joinColumn.substring(0, 1).toUpperCase() + joinColumn.substring(1);

                        // Obtém o método getter
                        Method joinColumnAttributeGetter = clazz.getMethod(getterName);

                        // Invoca o método getter para obter o valor do UUID
                        Object uuidEntityToJoin = joinColumnAttributeGetter.invoke(entity);

                        if (uuidEntityToJoin == null) {
                            throw new Exception("UUID is null");
                        }

                        // Cria uma nova instância da entidade associada
                        EntidadeSincronizavel rawJpaEntity = (EntidadeSincronizavel) method.getParameterTypes()[0].getConstructor().newInstance();

                        // Configura o ID da entidade associada
                        rawJpaEntity.setId((UUID) uuidEntityToJoin);

                        // Invoca o método setter para configurar a relação
                        method.invoke(entity, rawJpaEntity);
                    }
                } catch (Exception e) {
                    // Log e/ou tratamento de exceção, conforme necessário
                    e.printStackTrace();
                }
            }
        }
    }
    // Método para obter o nome do campo correspondente a um setter
    private static String getFieldNameFromSetter(Method method) {
        String methodName = method.getName();
        String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        return fieldName;
    }


    public static boolean isJpaEntitySetter(Method method) {
        if (!method.getName().startsWith("set")) return false;
        if (method.getParameterCount() != 1) return false;
        if (!void.class.equals(method.getReturnType())) return false;
        return method.getParameters()[0].getType().getAnnotation(Entity.class) != null;
    }

    // Método para verificar se um método é um getter
    public static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) return false;
        if (method.getParameterCount() != 0) return false;
        if (void.class.equals(method.getReturnType())) return false;
        return true;
    }

    // Método para obter o nome do campo correspondente a um getter
    private static String getFieldNameFromGetter(Method method) {
        String methodName = method.getName();
        String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        return fieldName;
    }

}

