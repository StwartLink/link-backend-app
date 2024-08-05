package br.com.linkagrotech.visita_service.sync.modelo;

import br.com.linkagrotech.visita_service.sync.modelo.Migration;

public class PullRequestWrapper {

    /**
     * timestamp da última sincronização no dispositivo requerinte
     */
    public Long lastPulledAt;

    /**
     * Versão atual do schema no dispositivo requerinte.
     */
    public Long shcemaVersion;

    /**
     * Objeto que encapsula informações da migration. Pode ser utilizado junto ao "shcemaVersion".
     */
    public Migration migration;

}
