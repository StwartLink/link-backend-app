package br.com.linkagrotech.visita_service.model;

import br.com.linkagrotech.visita_service.model.converter.DayOfWeekListConverter;
import br.com.linkagrotech.visita_service.sync.modelo.EntidadeSincronizavel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

/**
 * Para recorrências "2 vezes na semana, terça e quinta às 15:00", fazer:
 * tipoRecorrencia = semanal
 * momentoEspecifico = null
 * horario: "15:00:00"
 * diaSemana = List.of(TUESDAY,THURSDAY)
 * intervalo = 0 (toda semana)
 */
@Entity(name = "recorrencia")
@Data
public class Recorrencia extends EntidadeSincronizavel {
    /**
     * Tipo da recorrência
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recorrencia")
    private TipoRecorrencia tipoRecorrencia;

    /**
     * Caso recorrência específica (dia especificado, dia único etc). É guardado o instante correto.
     */
    @Column(name="momento_especifico")
    private Instant momentoEspecifico;


    /**
     * Horário definido para qualquer dia que o evento for ocorrer
     * Formato: "hh:mm:ss"
     */
    private LocalTime horario;


    /**
     * Para recorrências semanais
     */
    @Convert(converter = DayOfWeekListConverter.class)
    @Column(columnDefinition = "text[]")
    private List<DayOfWeek> diaSemana;

    /**
     * Número de espaçamento entre os eventos. A depender do tipoRecorrencia, ele muda de interpretação
     */
    private Integer intervalo;


}
