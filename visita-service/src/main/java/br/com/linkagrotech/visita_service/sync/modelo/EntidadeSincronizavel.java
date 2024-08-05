package br.com.linkagrotech.visita_service.sync.modelo;

import br.com.linkagrotech.visita_service.model.TipoVisita;
import br.com.linkagrotech.visita_service.model.Visita;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_type",
        visible = false
)
public abstract class EntidadeSincronizavel implements Serializable {

    /**
     * Id controlado pelo servidor
     * Novos registros (vindos do cliente) nunca têm syncId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long syncId;

    /**
     * Id gerado pelo cliente
     * Múltiplos clientes podem gerar o mesmo id
     */
    private Long id;

    /**
     * Data de criação da entidade no servidor
     */
    @Column(name = "created_at",updatable = false)
    private Date createdAt;

    /**
     * Data de último update no servidor
     */
    @Column(name = "updated_at")
    private Date updatedAt;


    /**
     * Data de soft-delete no servidor
     */
    @Column(name = "deleted_at")
    private Date deletedAt;

    /**
     * Identificador do dispositivo que enviou a entidade
     */
    private String dispositivo;

    @PrePersist
    protected void onCreate(){
        if(this.createdAt==null)
            this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }


}
