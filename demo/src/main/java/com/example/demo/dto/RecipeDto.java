package com.example.demo.dto;

import java.util.List;

import com.example.demo.domain.model.Recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private String name;
    private String description;
    private Double defaultMilkVolume;
    private Double defaultStarterAmount;
    private Double heatingTemperature;
    private Integer heatingDuration;
    private Double inoculationTemperature;
    private Double incubationTemperature;
    private Integer minIncubationTime;
    private Integer maxIncubationTime;
    private Integer refrigerationTime;
    private Recipe.DifficultyLevel difficulty;
    private String tips;
    private List<IngredientDto> ingredients;
}
