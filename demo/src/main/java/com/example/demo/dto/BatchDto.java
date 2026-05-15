package com.example.demo.dto;

import lombok.Data;

public class BatchDto {

    @Data
    public static class StartBatchRequest {
        private Long recipeId;
        private Double customMilkVolume;
        private Double customStarterAmount;
    }
    
    @Data
    public static class FailRequest {
        private String reason;
    }

}
