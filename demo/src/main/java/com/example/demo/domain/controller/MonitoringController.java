package com.example.demo.domain.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.model.TemperatureLog;
import com.example.demo.domain.model.YogurtBatch;
import com.example.demo.dto.MonitoringDto;
import com.example.demo.repository.TemperatureLogRepository;
import com.example.demo.repository.YogurtBatchRepository;
import com.example.demo.service.TemperatureControlService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor

@Tag(name = "Monitoreo", description = "Endpoints para supervisar el estado de los lotes y temperaturas")
public class MonitoringController {

    private final YogurtBatchRepository batchRepository;
    private final TemperatureLogRepository temperatureLogRepository;
    private final TemperatureControlService temperatureControlService;
    
    @GetMapping("/batches/active")
    @Operation(
        summary = "Obtener lotes activos",
        description = "Retorna todos los lotes que están en proceso (calentamiento, incubación, enfriamiento o refrigeración)"
    )
    @ApiResponse(responseCode = "200", description = "Lotes activos obtenidos correctamente")
    @ApiResponse(responseCode = "404", description = "No se encontraron lotes activos")
    public ResponseEntity<List<YogurtBatch>> getActiveBatches() {
        List<YogurtBatch> activeBatches = batchRepository.findByStatus(YogurtBatch.BatchStatus.INCUBATING);
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.HEATING));
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.COOLING));
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.REFRIGERATING));
        return ResponseEntity.ok(activeBatches);
    }
    
    @GetMapping("/batches/{batchId}/temperature")
    @Operation(
        summary = "Resumen de temperatura",
        description = "Obtiene el resumen de temperaturas (actual, máxima, mínima y promedio) de un lote específico"
    )
    @ApiResponse(responseCode = "200", description = "Resumen de temperatura obtenido correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    public ResponseEntity<MonitoringDto.TemperatureSummary> getBatchTemperatureSummary(@PathVariable Long batchId) {
        Double currentTemp = temperatureControlService.getCurrentTemperature(batchId);
        Double maxTemp = temperatureLogRepository.getMaxTemperatureByBatch(batchId);
        Double minTemp = temperatureLogRepository.getMinTemperatureByBatch(batchId);
        Double avgTemp = temperatureLogRepository.getAverageTemperatureByBatchAndType(
            batchId, TemperatureLog.LogType.INCUBATION);
        
        MonitoringDto.TemperatureSummary summary = MonitoringDto.TemperatureSummary.builder()
            .currentTemperature(currentTemp)
            .maximumTemperature(maxTemp)
            .minimumTemperature(minTemp)
            .averageTemperature(avgTemp)
            .build();
        
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/batches/{batchId}/temperature-logs")
    @Operation(
        summary = "Historial de temperaturas",
        description = "Obtiene los registros de temperatura de un lote, con opción de filtrar por rango de fechas"
    )
    @ApiResponse(responseCode = "200", description = "Registros obtenidos correctamente")
    @ApiResponse(responseCode = "400", description = "Parámetros de fecha inválidos")
    public ResponseEntity<List<TemperatureLog>> getTemperatureLogs(
            @PathVariable Long batchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        if (start != null && end != null) {
            return ResponseEntity.ok(temperatureLogRepository.findByBatchAndTimeRange(batchId, start, end));
        }
        
        YogurtBatch batch = batchRepository.findById(batchId).orElseThrow();
        return ResponseEntity.ok(temperatureLogRepository.findByBatch(batch));
    }
    
    @GetMapping("/dashboard")
    @Operation(
        summary = "Panel de monitoreo",
        description = "Obtiene estadísticas generales de los lotes como cantidades por estado, activos y completados hoy"
    )
    @ApiResponse(responseCode = "200", description = "Datos del dashboard obtenidos correctamente")
    public ResponseEntity<MonitoringDto.Dashboard> getDashboard() {
        long preparingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.PREPARING);
        long heatingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.HEATING);
        long coolingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.COOLING);
        long incubatingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.INCUBATING);
        long refrigeratingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.REFRIGERATING);
        long completedCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.COMPLETED);
        long failedCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.FAILED);
        
        Map<String, Long> batchCounts = new HashMap<>();
        batchCounts.put("PREPARING", preparingCount);
        batchCounts.put("HEATING", heatingCount);
        batchCounts.put("COOLING", coolingCount);
        batchCounts.put("INCUBATING", incubatingCount);
        batchCounts.put("REFRIGERATING", refrigeratingCount);
        batchCounts.put("COMPLETED", completedCount);
        batchCounts.put("FAILED", failedCount);
        
        MonitoringDto.Dashboard dashboard = MonitoringDto.Dashboard.builder()
            .batchCounts(batchCounts)
            .activeBatchesCount(preparingCount + heatingCount + coolingCount + incubatingCount + refrigeratingCount)
            .completedToday(batchRepository.findByStatusAndDateRange(
                YogurtBatch.BatchStatus.COMPLETED, 
                LocalDateTime.now().withHour(0).withMinute(0), 
                LocalDateTime.now()).size())
            .build();
        
        return ResponseEntity.ok(dashboard);
    }
}