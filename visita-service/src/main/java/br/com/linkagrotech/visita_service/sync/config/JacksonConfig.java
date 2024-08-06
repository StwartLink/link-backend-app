package br.com.linkagrotech.visita_service.sync.config;
import br.com.linkagrotech.visita_service.sync.modelo.serialize.CustomInstantDeserializer;
import br.com.linkagrotech.visita_service.sync.modelo.serialize.CustomInstantSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Instant;

@Configuration
public class JacksonConfig {

    @Bean(name = "defaultObjectMapper")
    @Primary
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }

    @Bean(name = "sincronizacaoObjectMapper")
    public ObjectMapper sincronizacaoObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Custom Introspector to ignore Hibernate relationship annotations
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public boolean hasIgnoreMarker(AnnotatedMember m) {
                return m.hasAnnotation(ManyToOne.class) ||
                        m.hasAnnotation(OneToMany.class) ||
                        m.hasAnnotation(OneToOne.class) ||
                        m.hasAnnotation(ManyToMany.class);
            }
        });

        // Register additional modules
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new EntitySchemaSubtypeModule());
        SimpleModule customModule = new SimpleModule();
        customModule.addSerializer(Instant.class, new CustomInstantSerializer());
        customModule.addDeserializer(Instant.class, new CustomInstantDeserializer());
        mapper.registerModule(customModule);


        return mapper;
    }

}

