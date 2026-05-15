package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ingrediente asociado a una receta")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del ingrediente", example = "1")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "Nombre del ingrediente", example = "Leche")
    private String name;
    
    @Schema(description = "Cantidad del ingrediente", example = "2.0")
    private Double quantity;
    
    @Schema(description = "Unidad de medida", example = "litros")
    private String unit; 
    
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonBackReference  // 🔥 ESTO ARREGLA EL ERROR
    @Schema(hidden = true)
    private Recipe recipe;
    
    @Schema(description = "Notas adicionales", example = "Debe estar fresca")
    private String notes;
    
    @Column(nullable = false)
    @Schema(description = "Indica si el ingrediente es opcional", example = "false")
    private Boolean optional;
}