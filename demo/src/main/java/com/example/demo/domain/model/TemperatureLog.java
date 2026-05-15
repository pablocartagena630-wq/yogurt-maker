package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "temperature_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Registro de temperatura asociado a un lote de yogurt")
public class TemperatureLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro", example = "1")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    @JsonBackReference // 🔥 rompe el ciclo con YogurtBatch
    @Schema(hidden = true)
    private YogurtBatch batch;
    
    @Column(nullable = false)
    @Schema(description = "Temperatura registrada en grados Celsius", example = "36.5")
    private Double temperature;
    
    @Column(nullable = false)
    @Schema(description = "Fecha y hora del registro")
    private LocalDateTime recordedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo de registro de temperatura")
    private LogType type;
    
    @Schema(description = "Notas adicionales del registro")
    private String notes;
    
    @Schema(description = "Tipos de registro de temperatura")
    public enum LogType {
        HEATING, COOLING, INCUBATION, REFRIGERATION, MANUAL
    }
}