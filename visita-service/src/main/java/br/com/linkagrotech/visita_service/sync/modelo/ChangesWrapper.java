package br.com.linkagrotech.visita_service.sync.modelo;



import lombok.Data;

import java.util.Map;

/**
 * Returned raw records MUST match your app's Schema
 * Returned raw records MUST NOT not contain special _status, _changed fields.
 * Returned raw records MAY contain fields (columns) that are not yet present in the local app (at schemaVersion -- but added in a later version). They will be safely ignored.
 */
@Data
public class ChangesWrapper {

    /**
     * Alterações realizadas
     */
    private Changes changes;

    /**
     * timestamp da última sincronização no dispositivo requerinte
     */
    private Long lastPulledAt;

    /**
    * Timestamp antes logo antes de realizar o pull
     */
    private Long timestamp;

    public void addChange(Map.Entry<String, Change> entryChange ){

        changes.getTableChanges().put(entryChange.getKey(),entryChange.getValue());

    }

}

