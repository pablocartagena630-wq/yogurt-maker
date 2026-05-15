package com.example.demo.domain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.model.YogurtBatch;
import com.example.demo.dto.BatchDto;
import com.example.demo.dto.TemperatureRecordDto;
import com.example.demo.service.YogurtMakingService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor

@Tag(name = "Gestión de Lotes", description = "Operaciones para crear y gestionar el proceso de producción de yogurt")
public class YogurtBatchController {
    
    private final YogurtMakingService yogurtMakingService;
    
    @PostMapping
    @Operation(
        summary = "Iniciar nuevo lote",
        description = "Crea un nuevo lote de producción basado en una receta y parámetros personalizados"
    )
    @ApiResponse(responseCode = "201", description = "Lote creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos enviados")
    public ResponseEntity<YogurtBatch> startNewBatch(@RequestBody BatchDto.StartBatchRequest request) {
        YogurtBatch batch = yogurtMakingService.startNewBatch(
            request.getRecipeId(), 
            request.getCustomMilkVolume(), 
            request.getCustomStarterAmount()
        );
        return new ResponseEntity<>(batch, HttpStatus.CREATED);
    }
    
    @PostMapping("/{batchId}/heating")
    @Operation(
        summary = "Iniciar calentamiento",
        description = "Inicia la fase de calentamiento de un lote"
    )
    @ApiResponse(responseCode = "200", description = "Calentamiento iniciado correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    public ResponseEntity<YogurtBatch> startHeating(@PathVariable Long batchId) {
        YogurtBatch batch = yogurtMakingService.startHeating(batchId);
        return ResponseEntity.ok(batch);
    }
    
    @PostMapping("/{batchId}/inoculating")
    @Operation(
        summary = "Iniciar inoculación",
        description = "Inicia la fase de inoculación del lote"
    )
    @ApiResponse(responseCode = "200", description = "Inoculación iniciada correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    public ResponseEntity<YogurtBatch> startInoculating(@PathVariable Long batchId) {
        YogurtBatch batch = yogurtMakingService.startInoculating(batchId);
        return ResponseEntity.ok(batch);
    }
    
    @PostMapping("/{batchId}/incubation")
    @Operation(
        summary = "Iniciar incubación",
        description = "Inicia la fase de incubación del lote"
    )
    @ApiResponse(responseCode = "200", description = "Incubación iniciada correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    public ResponseEntity<YogurtBatch> startIncubation(@PathVariable Long batchId) {
        YogurtBatch batch = yogurtMakingService.startIncubation(batchId);
        return ResponseEntity.ok(batch);
    }
    
    @PostMapping("/{batchId}/refrigeration")
    @Operation(
        summary = "Iniciar refrigeración",
        description = "Inicia la fase de refrigeración del lote"
    )
    @ApiResponse(responseCode = "200", description = "Refrigeración iniciada correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    public ResponseEntity<YogurtBatch> startRefrigeration(@PathVariable Long batchId) {
        YogurtBatch batch = yogurtMakingService.startRefrigeration(batchId);
        return ResponseEntity.ok(batch);
    }
    
    @PostMapping("/{batchId}/complete")
    @Operation(
        summary = "Completar lote",
        description = "Marca el lote como completado"
    )
    @ApiResponse(responseCode = "200", description = "Lote completado correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    public ResponseEntity<YogurtBatch> completeBatch(@PathVariable Long batchId) {
        YogurtBatch batch = yogurtMakingService.completeBatch(batchId);
        return ResponseEntity.ok(batch);
    }
    
    @PostMapping("/{batchId}/fail")
    @Operation(
        summary = "Marcar lote como fallido",
        description = "Marca un lote como fallido indicando el motivo"
    )
    @ApiResponse(responseCode = "200", description = "Lote marcado como fallido")
    @ApiResponse(responseCode = "400", description = "Datos inválidos enviados")
    public ResponseEntity<YogurtBatch> markAsFailed(
            @PathVariable Long batchId, 
            @RequestBody BatchDto.FailRequest request) {
        YogurtBatch batch = yogurtMakingService.markAsFailed(batchId, request.getReason());
        return ResponseEntity.ok(batch);
    }
    
    @GetMapping
    @Operation(
        summary = "Listar lotes",
        description = "Obtiene todos los lotes o filtra por estado si se proporciona un parámetro"
    )
    @ApiResponse(responseCode = "200", description = "Lista de lotes obtenida correctamente")
    public ResponseEntity<List<YogurtBatch>> getAllBatches(
            @RequestParam(required = false) YogurtBatch.BatchStatus status) {
        if (status != null) {
            return ResponseEntity.ok(yogurtMakingService.getBatchesByStatus(status));
        }
        return ResponseEntity.ok(yogurtMakingService.getAllBatches());
    }
    
    @GetMapping("/{batchId}")
    @Operation(
        summary = "Obtener lote",
        description = "Obtiene la información de un lote específico por su ID"
    )
    @ApiResponse(responseCode = "200", description = "Lote encontrado")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    public ResponseEntity<YogurtBatch> getBatch(@PathVariable Long batchId) {
        YogurtBatch batch = yogurtMakingService.getBatch(batchId);
        return ResponseEntity.ok(batch);
    }
    
    @PostMapping("/{batchId}/temperature")
    @Operation(
        summary = "Registrar temperatura",
        description = "Registra una nueva medición de temperatura para un lote"
    )
    @ApiResponse(responseCode = "200", description = "Temperatura registrada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos enviados")
    public ResponseEntity<Void> recordTemperature(
            @PathVariable Long batchId, 
            @RequestBody TemperatureRecordDto request) {
        yogurtMakingService.recordTemperature(batchId, request.getTemperature(), request.getType());
        return ResponseEntity.ok().build();
    }
}