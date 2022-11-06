package com.example.nodb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.nodb.model.Employee;
import com.example.nodb.service.EmployeeServiceStub;

@RestController
public class EmployeeController {
		
	@GetMapping("/employees")
	public List<Employee> getAllEmployees(){
		return EmployeeServiceStub.getAllEmployees();
	}
	
	@GetMapping("/employee/{employeeId}")
	public Employee getEmployeeDetails(@PathVariable Long employeeId) {
		return EmployeeServiceStub.getEmployeeDetails(employeeId);
	}
	
	@PostMapping("/addEmployee")
	public Employee addEmployee(@RequestBody Employee employee) {
		return EmployeeServiceStub.addEmployee(employee);
	}
	
	@PutMapping("/updateEmployee/{employeeId}")
	public Employee updateEmployee(@PathVariable Long employeeId,@RequestBody Employee employee) {
		return EmployeeServiceStub.updateEmployee(employeeId, employee);
	}
	
	@DeleteMapping("/deleteEmployee/{employeeId}")
		public Employee deleteEmployee(@PathVariable Long employeeId) {
			return EmployeeServiceStub.deleteEmployee(employeeId);
		}
	
		@PutMapping("/lockEmployee/{employeeId}")
		public Long lockEmployee(@PathVariable Long employeeId) {
			return EmployeeServiceStub.lockEmployee(employeeId);
		}
		
		@PutMapping("/unlockEmployee/{employeeId}")
		public Long unlockEmployee(@PathVariable Long employeeId) {
			return EmployeeServiceStub.unlockEmployee(employeeId);
		}

		

		

}