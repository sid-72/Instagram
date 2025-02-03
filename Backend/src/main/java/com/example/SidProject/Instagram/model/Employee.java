package com.example.SidProject.Instagram.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "Employees")
public class Employee {
    @Id
    private Integer id;
    private String name;
    private String company;
    private Integer age;
}
