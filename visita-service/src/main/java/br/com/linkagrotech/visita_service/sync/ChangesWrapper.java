package br.com.linkagrotech.visita_service.sync;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Returned raw records MUST match your app's Schema
 * Returned raw records MUST NOT not contain special _status, _changed fields.
 * Returned raw records MAY contain fields (columns) that are not yet present in the local app (at schemaVersion -- but added in a later version). They will be safely ignored.
 */
public class ChangesWrapper {

    /**
     * Alterações realizadas
     */
    public Changes changes;

    /**
    * Timestamp antes logo antes de realizar a primeira consulta de "changes"
     */
    public Long timestamp;

    public void addChange(Map.Entry<String, Change> entryChange ){

        changes.tableChanges.put(entryChange.getKey(),entryChange.getValue());

    }

}

/*
{
    "changes": {
        "tableChanges": {
            "visita": {
                "created": [
                    {
                        "type":"visita",
                        "dispositivo": "MAC::99800.00082.3321",
                        "syncId": null,
                        "id": 16,
                        "createdAt": null,
                        "updatedAt": null,
                        "deletedAt": null,
                        "tipoVisita": {
                            "descricao": "top dsaadsadsasd descricoes"
                        },
                        "cliente": null
                    },
                    {
                        "type":"visita",
                        "dispositivo": "MAC::99800.00082.321",
                        "syncId": null,
                        "id": 17,
                        "createdAt": null,
                        "updatedAt": null,
                        "deletedAt": null,
                        "tipoVisita": {
                            "descricao": "descricao diferenciada"
                        },
                        "cliente": null
                    }
                ],
                "updated": [],
                "deleted": []
            }
        }
    },
    "timestamp": 1722632881023
}
 */
