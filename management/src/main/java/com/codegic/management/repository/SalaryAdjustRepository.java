package com.codegic.management.repository;

import com.codegic.management.entity.SalaryAdjustHistory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface SalaryAdjustRepository extends JpaRepository<SalaryAdjustHistory, Long> {
    Optional<SalaryAdjustHistory> findTopByDepartmentIdOrderByAdjustmentTimeDesc(Long departmentId);
}
