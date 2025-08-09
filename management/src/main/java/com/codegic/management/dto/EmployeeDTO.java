package com.codegic.management.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private Double salary;
    private LocalDate joiningDate;
    private Long departmentId;
}