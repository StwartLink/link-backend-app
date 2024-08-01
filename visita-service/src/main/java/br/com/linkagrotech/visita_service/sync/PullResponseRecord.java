package br.com.linkagrotech.visita_service.sync;

import lombok.Data;

/**
 * Returned raw records MUST match your app's Schema
 * Returned raw records MUST NOT not contain special _status, _changed fields.
 * Returned raw records MAY contain fields (columns) that are not yet present in the local app (at schemaVersion -- but added in a later version). They will be safely ignored.
 */
@Data
public class PullResponseRecord {

    /**
     * Alterações realizadas
     */
    Changes changes;

    /**
    * Timestamp antes logo antes de realizar a primeira consulta de "changes"
     */
    Long timestamp;
}
