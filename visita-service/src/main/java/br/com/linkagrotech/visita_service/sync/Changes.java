package br.com.linkagrotech.visita_service.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class Changes {

    public Map<String,Change> tableChanges;

    public Changes(Map.Entry<String,Change> tableChanges){
        this.tableChanges = Map.ofEntries(tableChanges);
    }


    @SafeVarargs
    public static  Changes of(Map.Entry<String,Change>... tableChanges){
        Changes changes = new Changes();
        changes.tableChanges = Map.ofEntries(tableChanges);
        return changes;
    }

}
