package com.example.SidProject.Instagram.Services;

import com.example.SidProject.Instagram.model.Employee;
import com.example.SidProject.Instagram.repositories.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private ObjectMapper objectMapper;


    public EmployeeService(EmployeeRepository employeeRepository, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
    }


    public Employee getEmployeeById(Integer id) {
        String json = "{\"name\":\"John Doe\",\"id\":30}";
        try {
            System.out.println(objectMapper.readValue(json, Employee.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return employeeRepository.getReferenceById(id);
    }


}
