package com.example.nodb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.example.nodb.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.Logger;

public class EmployeeServiceStub {
	
	public static List<Employee> getAllEmployees(){
		List<Employee> emps =new ArrayList<Employee>();
		try {
			ObjectMapper mapper= new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			InputStream inputStream=new FileInputStream(new File("C:/Person/person.json"));
			TypeReference<List<Employee>> typeReference=new TypeReference<List<Employee>>() {};
			//READ FROM FILE
					emps = mapper.readValue(inputStream,typeReference);
					
					inputStream.close();		
					
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(JsonParseException e) {
			e.printStackTrace();
		}catch(JsonMappingException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return emps;
		
		
	}
	
	@Cacheable(cacheNames = "employees", key = "#employeeId")
	public static Employee getEmployeeDetails(Long employeeId) throws InterruptedException  {
		
		List<Employee> emps=new ArrayList<Employee>();
		boolean flag=false;
		Employee res=new Employee();
		try {
			ObjectMapper mapper= new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			InputStream inputStream=new FileInputStream(new File("C:/Person/person.json"));
			TypeReference<List<Employee>> typeReference=new TypeReference<List<Employee>>() {};
			//READ FROM FILE
			
			       emps=mapper.readValue(inputStream,typeReference);
					for(Employee emp : emps) {
						if(emp.getId()==employeeId) {
							res = emp;
							flag = true;
							break;
						}
					}
					
					inputStream.close();		
					
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(JsonParseException e) {
			e.printStackTrace();
		}catch(JsonMappingException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}

		if(flag == true) {
			return res;
		}
		else {
			return null;
		}
		
			
	}
	
	
	public static Employee addEmployee(Employee employee) {
		List<Employee> emps =new ArrayList<Employee>();
		try {
			ObjectMapper mapper= new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			InputStream inputStream=new FileInputStream(new File("C:/Person/person.json"));
			TypeReference<List<Employee>> typeReference=new TypeReference<List<Employee>>() {};
			//READ FROM FILE
					emps = mapper.readValue(inputStream,typeReference);
					for(Employee emp : emps) {
						if(emp.getId() == employee.getId()) {
							return null;
						}
					}
			
					emps.add(employee);
					
			//write after adding new object
					mapper.writeValue(new File("C:/Person/person.json"),emps);
					
					inputStream.close();		
					
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(JsonParseException e) {
			e.printStackTrace();
		}catch(JsonMappingException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}

		
			return employee;
		
	}
	
	
	@CachePut(cacheNames = "employees", key = "#emps.employeeId")
	public static synchronized Employee updateEmployee(Long employeeId,Employee employee) {
		List<Employee> emps = new ArrayList<Employee>();
		boolean flag=false;
		try {
			ObjectMapper mapper= new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			InputStream inputStream=new FileInputStream(new File("C:/Person/person.json"));
			TypeReference<List<Employee>> typeReference=new TypeReference<List<Employee>>() {};
			//READ FROM FILE
			
					emps = mapper.readValue(inputStream,typeReference);
					for(Employee emp : emps) {
						if(emp.getId() == employeeId) {
							if(emp.isLock()) {
								return null;
							}
							emps.remove(emp);
							flag=true;
							break;
						}
					}
					emps.add(employee);
					Logger logger = (Logger) LoggerFactory.getLogger(EmployeeServiceStub.class);
					logger.info("updated");
					mapper.writeValue(new File("C:/Person/person.json"),emps);
					
					inputStream.close();		
					
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(JsonParseException e) {
			e.printStackTrace();
		}catch(JsonMappingException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}

		if(flag==true) {
			return employee;
		}
		else {
			return null;
		}
	}
	
	
	@CacheEvict(cacheNames = "employees" , key = "#employeeId")
	public static synchronized Employee deleteEmployee(Long employeeId) {
		List<Employee> emps = new ArrayList<Employee>();
		boolean flag=false;
		Employee res=new Employee();
		try {
			ObjectMapper mapper= new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			InputStream inputStream=new FileInputStream(new File("C:/Person/person.json"));
			TypeReference<List<Employee>> typeReference=new TypeReference<List<Employee>>() {};
			//READ FROM FILE
			
					emps = mapper.readValue(inputStream,typeReference);
					for(Employee emp : emps) {
						if(emp.getId()==employeeId) {
							if(emp.isLock()) {
								return null;
							}
							res=emp;
							emps.remove(emp);
							flag=true;
							break;
						}
					}
					
			//write after deletion
					mapper.writeValue(new File("C:/Person/person.json"),emps);
					
					inputStream.close();		
					
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(JsonParseException e) {
			e.printStackTrace();
		}catch(JsonMappingException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}

		if(flag==true) {
			return res;
		}
		else {
			return null;
		}
	
	}
		
	public static  Long lockEmployee(Long employeeId) {
		List<Employee> emps =new ArrayList<Employee>();
		boolean flag=false;
		try {
			ObjectMapper mapper= new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			InputStream inputStream=new FileInputStream(new File("C:/Person/person.json"));
			TypeReference<List<Employee>> typeReference=new TypeReference<List<Employee>>() {};
			//READ FROM FILE
			
					emps = mapper.readValue(inputStream,typeReference);
					for(Employee emp : emps) {
						if(emp.getId() == employeeId) {
							if(emp.isLock()) {
								return null;
							}
							emp.setLock(true);
							flag=true;
							break;
						}
					}
					
			//write after deletion
					mapper.writeValue(new File("C:/Person/person.json"),emps);
					
					inputStream.close();		
					
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(JsonParseException e) {
			e.printStackTrace();
		}catch(JsonMappingException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}

		if(flag==true) {
			return employeeId;
		}
		else {
			return null;
		}
	}
	
	//UNLOCK
	public static  Long unlockEmployee(Long employeeId)  {
		List<Employee> emps =new ArrayList<Employee>();
		boolean flag=false;
		try {
			ObjectMapper mapper= new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			InputStream inputStream=new FileInputStream(new File("C:/Person/person.json"));
			TypeReference<List<Employee>> typeReference=new TypeReference<List<Employee>>() {};
			//READ FROM FILE
			
					emps = mapper.readValue(inputStream,typeReference);
					for(Employee emp : emps) {
						if(emp.getId() == employeeId) {
							if(!emp.isLock()) {
								return null;
							}
							emp.setLock(false);
							flag=true;
							break;
						}
					}
					
			//write after deletion
					mapper.writeValue(new File("C:/Person/person.json"),emps);
					
					inputStream.close();		
					
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(JsonParseException e) {
			e.printStackTrace();
		}catch(JsonMappingException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}

		if(flag==true) {
			return employeeId;
		}
		else {
			return null;
		}
				
		
	}
			
	
	
	
	
}