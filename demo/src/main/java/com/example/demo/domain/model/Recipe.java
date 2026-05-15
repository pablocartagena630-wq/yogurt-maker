package com.example.demo.domain.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa una receta de yogurt")
public class Recipe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la receta", example = "1")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de la receta", example = "Yogurt Natural")
    private String name;
    
    @Schema(description = "Descripción de la receta")
    private String description;
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Builder.Default
    @Schema(description = "Lista de ingredientes asociados a la receta")
    private List<Ingredient> ingredients = new ArrayList<>();
    
    @Column(nullable = false)
    @Schema(description = "Volumen de leche por defecto en litros", example = "2.0")
    private Double defaultMilkVolume; 
    
    @Column(nullable = false)
    @Schema(description = "Cantidad de fermento por defecto en cucharadas", example = "0.5")
    private Double defaultStarterAmount; 
    
    @Column(nullable = false)
    @Schema(description = "Temperatura de calentamiento en grados Celsius", example = "85.0")
    private Double heatingTemperature; 
    
    @Column(nullable = false)
    @Schema(description = "Duración del calentamiento en minutos", example = "30")
    private Integer heatingDuration; 
    
    @Column(nullable = false)
    @Schema(description = "Temperatura de inoculación en grados Celsius", example = "42.0")
    private Double inoculationTemperature; 
    
    @Column(nullable = false)
    @Schema(description = "Temperatura de incubación en grados Celsius", example = "37.0")
    private Double incubationTemperature; 
    
    @Column(nullable = false)
    @Schema(description = "Tiempo mínimo de incubación en horas", example = "6")
    private Integer minIncubationTime; 
    
    @Column(nullable = false)
    @Schema(description = "Tiempo máximo de incubación en horas", example = "12")
    private Integer maxIncubationTime; 
    
    @Column(nullable = false)
    @Schema(description = "Tiempo de refrigeración en horas", example = "4")
    private Integer refrigerationTime; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Nivel de dificultad de la receta")
    private DifficultyLevel difficulty;
    
    @Schema(description = "Consejos adicionales para la preparación")
    private String tips;
    
    @Column(nullable = false)
    @Schema(description = "Indica si la receta está activa", example = "true")
    private Boolean active;
    
    @Schema(description = "Niveles de dificultad disponibles")
    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
}