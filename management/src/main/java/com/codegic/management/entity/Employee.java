package com.codegic.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Positive
    private Double salary;

    @PastOrPresent
    private LocalDate joiningDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
