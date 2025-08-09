package com.codegic.management.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SalaryAdjustRequest {
    @NotNull
    private Long departmentId;

    @Min(0) @Max(100)
    private int performanceScore;
}