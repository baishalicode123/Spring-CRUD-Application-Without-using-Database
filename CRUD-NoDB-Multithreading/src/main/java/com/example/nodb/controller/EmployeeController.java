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
	public synchronized Employee addEmployee(@RequestBody Employee employee) throws InterruptedException{
		return EmployeeServiceStub.addEmployee(employee);
	}
	
	@PutMapping("/updateEmployee/{employeeId}")
	public synchronized Employee updateEmployee(@PathVariable Long employeeId,@RequestBody Employee employee) throws InterruptedException{
		wait(100);
		return EmployeeServiceStub.updateEmployee(employeeId, employee);
	}
	
	@DeleteMapping("/deleteEmployee/{employeeId}")
		public synchronized Employee deleteEmployee(@PathVariable Long employeeId) throws InterruptedException {
			return EmployeeServiceStub.deleteEmployee(employeeId);
		}
	
		@PutMapping("/lockEmployee/{employeeId}")
		public Long lockEmployee(@PathVariable Long employeeId) throws InterruptedException {
			return EmployeeServiceStub.lockEmployee(employeeId);
		}
		
		@PutMapping("/unlockEmployee/{employeeId}")
		public Long unlockEmployee(@PathVariable Long employeeId) throws InterruptedException {
			return EmployeeServiceStub.unlockEmployee(employeeId);
		}

		

		

}