package br.com.linkagrotech.visita_service.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
public class Changes {

    Map<String,Change<? extends EntidadeSincronizavel>> tableChanges;

    public Changes(Map<String,Change<? extends EntidadeSincronizavel>> tableChanges){
        this.tableChanges = tableChanges;
    }

}
