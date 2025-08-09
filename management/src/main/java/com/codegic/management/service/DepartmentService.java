package com.codegic.management.service;

import com.codegic.management.dto.DepartmentDTO;
import com.codegic.management.entity.Department;
import com.codegic.management.exception.ResourceNotFoundException;
import com.codegic.management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department department = new Department(null, dto.getName(), dto.getCode());
        Department saved = departmentRepository.save(department);
        return new DepartmentDTO(saved.getId(), saved.getName(), saved.getCode());
    }

    public DepartmentDTO getDepartment(Long id) {
        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return new DepartmentDTO(dep.getId(), dep.getName(), dep.getCode());
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        dep.setName(dto.getName());
        dep.setCode(dto.getCode());
        Department updated = departmentRepository.save(dep);
        return new DepartmentDTO(updated.getId(), updated.getName(), updated.getCode());
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
