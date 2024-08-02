package br.com.linkagrotech.visita_service.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Changes<T extends EntidadeSincronizavel> {

    private Map<String,Change<T>> tableChanges;

    public Changes(Map.Entry<String,Change<T>> tableChanges){
        this.tableChanges = Map.ofEntries(tableChanges);
    }


    @SafeVarargs
    public static <T extends EntidadeSincronizavel> Changes<T> of(Map.Entry<String,Change<T>>... tableChanges){
        Changes<T> changes = new Changes<>();
        changes.tableChanges = Map.ofEntries(tableChanges);
        return changes;
    }

}
