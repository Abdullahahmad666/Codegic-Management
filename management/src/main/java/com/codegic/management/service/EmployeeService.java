package com.codegic.management.service;

import com.codegic.management.dto.EmployeeDTO;
import com.codegic.management.dto.SalaryAdjustRequest;
import com.codegic.management.entity.Department;
import com.codegic.management.entity.Employee;
import com.codegic.management.exception.DuplicateAdjustmentException;
import com.codegic.management.exception.ResourceNotFoundException;
import com.codegic.management.repository.DepartmentRepository;
import com.codegic.management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final SalaryAdjustHistoryService historyService;

    public EmployeeService(EmployeeRepository employeeRepository,DepartmentRepository departmentRepository,SalaryAdjustHistoryService historyService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.historyService = historyService;
    }

    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Employee emp = new Employee(null, dto.getName(), dto.getEmail(), dto.getSalary(),
                dto.getJoiningDate(), department);

        Employee saved = employeeRepository.save(emp);

        return mapToDTO(saved);
    }

    public EmployeeDTO getEmployee(Long id) {
        return employeeRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> list = new ArrayList<>();
        for (Employee emp : employees) {
            list.add(mapToDTO(emp));
        }
        return list;
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setSalary(dto.getSalary());
        emp.setJoiningDate(dto.getJoiningDate());
        emp.setDepartment(department);

        Employee updated = employeeRepository.save(emp);
        return mapToDTO(updated);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // --- Business Logic: Adjust Salary ---
    public String adjustSalary(SalaryAdjustRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        // Idempotency check from DB (30 minutes limit)
        if (historyService.wasAdjustedRecently(department.getId(), 30)) {
            throw new DuplicateAdjustmentException("Salary adjustment already done in last 30 minutes.");
        }

        List<Employee> employees = employeeRepository.findByDepartmentId(department.getId());
        double performanceIncrease = 0.0;

        // Performance-based increase
        if (request.getPerformanceScore() >= 90) {
            performanceIncrease = 0.15;
        } else if (request.getPerformanceScore() >= 70) {
            performanceIncrease = 0.10;
        } else {
            System.out.println("Warning: No salary increase due to low performance.");
            return "No salary increase due to low performance score.";
        }

        for (Employee emp : employees) {
            double newSalary = emp.getSalary() + (emp.getSalary() * performanceIncrease);

            // Hidden tenure bonus (>5 years)
            if (emp.getJoiningDate().isBefore(LocalDate.now().minusYears(5))) {
                newSalary += emp.getSalary() * 0.05;
            }

            // Salary cap
            if (newSalary > 200_000) {
                newSalary = 200_000;
            }

            emp.setSalary(newSalary);
        }

        employeeRepository.saveAll(employees);

        // Record adjustment in DB
        historyService.recordAdjustment(department.getId());

        return "Salary adjustment completed for department: " + department.getName();
    }

    // --- Helper ---
    private EmployeeDTO mapToDTO(Employee emp) {
        return new EmployeeDTO(emp.getId(), emp.getName(), emp.getEmail(),
                emp.getSalary(), emp.getJoiningDate(),
                emp.getDepartment() != null ? emp.getDepartment().getId() : null);
    }
}
