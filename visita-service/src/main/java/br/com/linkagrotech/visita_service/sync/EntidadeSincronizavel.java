package br.com.linkagrotech.visita_service.sync;

import br.com.linkagrotech.visita_service.model.Visita;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Visita.class, name = "visita")
})
public abstract class EntidadeSincronizavel {

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
    @Column(name = "created_at")
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


}
