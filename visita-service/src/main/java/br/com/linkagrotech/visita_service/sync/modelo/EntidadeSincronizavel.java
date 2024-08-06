package br.com.linkagrotech.visita_service.sync.modelo;

import br.com.linkagrotech.visita_service.sync.modelo.annotations.ClientId;
import br.com.linkagrotech.visita_service.sync.modelo.annotations.VersaoSchema;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * COMO USAR:
 * Faça sua entidade herdar de EntidadeSincronizavel
 * Para cada relação que a entidade que herda dessa tiver, no caso de não ser uma lista (OneToOne e ManyToOne), você
 * deve :
 *  - inserir as annotations "@JsonIgnore",
 *  - colocar fetch = FetchType.EAGER
 *  - colocar a annotation @JoinColumn(name = 'nome_join_column')
 *  - criar um atributo do tipo UUID anotado com @Transient de mesmo nome de 'nome_join_column'
 * Com isso, a entidade estará pronta para ser utilizada no ServicoEntidadeSincronizavel para consultas e sincronizações.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_type",
        visible = false
)
@VersaoSchema(versao = 1L)
public abstract class EntidadeSincronizavel implements Serializable {


//    /**
//     * Id controlado pelo servidor
//     * Novos registros (vindos do cliente) nunca têm syncId
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long syncId;

    /**
     * Id gerado pelo cliente
     * Múltiplos clientes podem gerar o mesmo id
     */
    @Id
    private UUID id;

//    /**
//     * Identificador do dispositivo que enviou a entidade
//     */
//    private String dispositivo;

    /**
     * Data de criação da entidade no servidor
     */
    @Column(name = "created_at",updatable = false)
    private Instant createdAt;

    /**
     * Data de último update no servidor
     */
    @Column(name = "updated_at")
    private Instant updatedAt;


    /**
     * Data de soft-delete no servidor
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;



    @PrePersist
    protected void onCreate(){

        if(this.createdAt==null)
            this.createdAt = Instant.now();

        if (this.id == null) {
            this.id = UUID.randomUUID();
        }

    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }


}
