package br.com.linkagrotech.visita_service.sync.config;

import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import jakarta.persistence.Entity;
import org.reflections.Reflections;

import java.util.Set;

public class DynamicSubtypeModule extends SimpleModule {

    public static String toSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public DynamicSubtypeModule() {
        Reflections reflections = new Reflections("br.com.linkagrotech");
        Set<Class<? extends EntidadeSincronizavel>> subTypes = reflections.getSubTypesOf(EntidadeSincronizavel.class);

        for (Class<? extends EntidadeSincronizavel> subType : subTypes) {
            Entity entityAnnotation = subType.getAnnotation(Entity.class);
            if (entityAnnotation != null) {
                String name = entityAnnotation.name();
                if (name.isEmpty()) {
                    name = toSnakeCase(subType.getSimpleName());
                }
                this.registerSubtypes(new NamedType(subType, name));
            }
        }
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.registerSubtypes(EntidadeSincronizavel.class);
    }
}
