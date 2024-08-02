package br.com.linkagrotech.visita_service.sync;

public class EntidadeSincronizavelIdentificadorDTO {

    public final Long id;
    public final String dispositivo;
    public final Long syncId;

    public EntidadeSincronizavelIdentificadorDTO(Long id, String dispositivo, Long syncId ){
        this.id = id;
        this.dispositivo = dispositivo;
        this.syncId = syncId;
    }

    public boolean match(Long id, String dispositivo){
        return id.equals(this.id) && dispositivo.equals(this.dispositivo);
    }

}
