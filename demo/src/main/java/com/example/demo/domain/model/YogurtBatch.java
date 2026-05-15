package com.example.demo.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "yogurt_batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa un lote de producción de yogurt")
public class YogurtBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del lote", example = "1")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "Código único del lote", example = "YB-123456")
    private String batchCode;
    
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    @Schema(description = "Receta asociada al lote")
    private Recipe recipe;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado actual del lote")
    private BatchStatus status;
    
    @Column(nullable = false)
    @Schema(description = "Volumen de leche en litros", example = "2.0")
    private Double milkVolume;
    
    @Column(nullable = false)
    @Schema(description = "Cantidad de fermento en cucharadas", example = "0.5")
    private Double starterAmount;
    
    @Column(nullable = false)
    @Schema(description = "Temperatura objetivo en grados Celsius", example = "37.0")
    private Double targetTemperature;
    
    @Column(nullable = false)
    @Schema(description = "Tiempo de incubación en horas", example = "8")
    private Integer incubationTime;
    
    @Schema(description = "Fecha y hora de inicio del lote")
    private LocalDateTime startTime;

    @Schema(description = "Inicio de la incubación")
    private LocalDateTime incubationStartTime;

    @Schema(description = "Fin de la incubación")
    private LocalDateTime incubationEndTime;

    @Schema(description = "Inicio de la refrigeración")
    private LocalDateTime refrigerationStartTime;
    
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonManagedReference  // 🔥 evita bucle infinito
    @Schema(description = "Registros de temperatura del lote")
    private List<TemperatureLog> temperatureLogs = new ArrayList<>();
    
    @Schema(description = "Notas adicionales")
    private String notes;
    
    @Column(nullable = false)
    @Schema(description = "Fecha de creación del lote")
    private LocalDateTime createdAt;
    
    @Schema(description = "Fecha de última actualización")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        batchCode = "YB-" + System.currentTimeMillis();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Schema(description = "Estados posibles del lote")
    public enum BatchStatus {
        PREPARING, 
        HEATING, 
        COOLING, 
        INOCULATING, 
        INCUBATING, 
        REFRIGERATING, 
        COMPLETED, 
        FAILED
    }
}