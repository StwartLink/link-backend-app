package br.com.linkagrotech.visita_service.sync.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class Changes {

    private Map<String, Change> tableChanges;


    /**
     * Obtem o objeto Changes para a lista de Entrys passados.
     * @param tableChanges
     * @return
     */
    @SafeVarargs
    public static  Changes of(Map.Entry<String,Change>... tableChanges){
        Changes changes = new Changes();
        changes.tableChanges = Map.ofEntries(tableChanges);
        return changes;
    }

}
