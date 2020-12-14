package com.compx.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compx.model.Employee;

public interface EmployeeRepository  extends MongoRepository<Employee, String> {
   public Employee save(Employee employee);
}
