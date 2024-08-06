package br.com.linkagrotech.visita_service.sync.modelo;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Change {

    private List<EntidadeSincronizavel> created;

    private List<EntidadeSincronizavel> updated;

    private List<UUID> deleted;

    public boolean possuiAlteracoes(){
        return !((created==null || created.isEmpty()) && (updated==null || updated.isEmpty()) && (deleted==null || deleted.isEmpty()));
    }

}
