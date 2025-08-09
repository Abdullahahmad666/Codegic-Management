package com.codegic.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codegic.management.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {}