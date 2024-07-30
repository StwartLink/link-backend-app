package br.com.linkagrotech.visita_service.sync;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class EntidadeSincronizavel {

    /**
     * Id controlado pelo servidor
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long syncId;

    /**
     * Id gerado pelo cliente
     */
    Long id;

    /**
     * Data de criação da entidade no servidor
     */
    @Column(name = "created_at")
    Date createdAt;

    /**
     * Data de último update no servidor
     */
    @Column(name = "updated_at")
    Date updatedAt;


    /**
     * Data de soft-delete no servidor
     */
    @Column(name = "deleted_at")
    Date deletedAt;


}
