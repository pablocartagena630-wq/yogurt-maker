package com.example.demo.domain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.model.Recipe;
import com.example.demo.dto.RecipeDto;
import com.example.demo.service.RecipeService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor

@Tag(name = "Gestión de Recetas", description = "Operaciones para crear, actualizar, buscar y gestionar recetas")
public class RecipeController {

    private final RecipeService recipeService;
    
    @PostMapping
    @Operation(
        summary = "Crear receta",
        description = "Crea una nueva receta con sus ingredientes y parámetros de preparación"
    )
    @ApiResponse(responseCode = "201", description = "Receta creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos enviados")
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = recipeService.createRecipe(recipeDto);
        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar receta",
        description = "Actualiza los datos de una receta existente mediante su ID"
    )
    @ApiResponse(responseCode = "200", description = "Receta actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody RecipeDto recipeDto) {
        Recipe recipe = recipeService.updateRecipe(id, recipeDto);
        return ResponseEntity.ok(recipe);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener receta por ID",
        description = "Obtiene una receta específica utilizando su identificador"
    )
    @ApiResponse(responseCode = "200", description = "Receta encontrada")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipe(id);
        return ResponseEntity.ok(recipe);
    }
    
    @GetMapping
    @Operation(
        summary = "Listar recetas",
        description = "Obtiene todas las recetas activas registradas en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de recetas obtenida correctamente")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllActiveRecipes());
    }
    
    @GetMapping("/search")
    @Operation(
        summary = "Buscar recetas",
        description = "Busca recetas por una palabra clave en su nombre o descripción"
    )
    @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente")
    @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String keyword) {
        return ResponseEntity.ok(recipeService.searchRecipes(keyword));
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(
        summary = "Desactivar receta",
        description = "Desactiva una receta para que no esté disponible en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Receta desactivada correctamente")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    public ResponseEntity<Void> deactivateRecipe(@PathVariable Long id) {
        recipeService.deactivateRecipe(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    @Operation(
        summary = "Activar receta",
        description = "Activa una receta previamente desactivada"
    )
    @ApiResponse(responseCode = "200", description = "Receta activada correctamente")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    public ResponseEntity<Void> activateRecipe(@PathVariable Long id) {
        recipeService.activateRecipe(id);
        return ResponseEntity.ok().build();
    }
}