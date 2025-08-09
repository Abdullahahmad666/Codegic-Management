package com.codegic.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codegic.management.entity.Employee;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
}