package br.com.linkagrotech.visita_service.sync;

import lombok.Data;

@Data
public class PullRequestRecord{

    /**
     * timestamp da última sincronização no dispositivo requerinte
     */
    Long lastPulledAt;

    /**
     * Versão atual do schema no dispositivo requerinte.
     */
    Long shcemaVersion;

    /**
     * Objeto que encapsula informações da migration. Pode ser utilizado junto ao "shcemaVersion".
     */
    Migration migration;

}
