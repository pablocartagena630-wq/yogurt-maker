package com.example.demo.dto;

import com.example.demo.domain.model.TemperatureLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Registro de temperatura")
public class TemperatureRecordDto {

    @Schema(description = "Valor de temperatura", example = "36.5")
    private Double temperature;

    @Schema(description = "Tipo de medición", example = "HEATING")
    private TemperatureLog.LogType type;
}