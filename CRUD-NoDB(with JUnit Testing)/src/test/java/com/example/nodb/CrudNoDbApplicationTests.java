package com.example.nodb;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.nodb.controller.EmployeeController;
import com.example.nodb.model.Employee;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class CrudNoDbApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//	
//	
	@Autowired
	EmployeeController eControl;
	

	@Test
	@Order(1)
	public void testCreate() {
		Employee e =new Employee();
		e.setId(1001);
		e.setFirstname("asdf");
		e.setLastname("hjkl");
		e.setEmail("jnkla@gmail.com");
		eControl.addEmployee(e);
		assertNotNull(eControl.getEmployeeDetails((long)1001));    //getting the details and checking assert not null
	}
	
	@Test
	@Order(2)
	public void testGetAll() {
		List<Employee> emp = eControl.getAllEmployees();
		assertThat(emp); 
	}

	private void assertThat(List<Employee> emp) {
		// TODO Auto-generated method stub
		
	}
	
	@Test
	@Order(3)
	public void testOneEmployee() {
		Employee emp = eControl.getEmployeeDetails((long) 1001);
		assertEquals("asdf", emp.getFirstname());
		
	}
	
	@Test
	@Order(4)
	public void testUpdate() {
		Employee emp =new Employee();
		emp.setId(1001);
		emp.setFirstname("aaab");
		emp.setLastname("c");
		emp.setEmail("abab@gmail.com");
		eControl.updateEmployee((long)1001, emp);
		assertNotEquals("abcd", eControl.getEmployeeDetails((long)1001).getLastname());
		
	}
	
	@Test
	@Order(5)
	public void testDelete() {
		eControl.deleteEmployee(1001L);
	    assertThat(eControl.getEmployeeDetails(1001L));
	}

	private Object assertThat(Employee employeeDetails) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Test
	@Order(6)
	public void testLock() {
//		Employee emp = new Employee();
		Employee emp = eControl.getEmployeeDetails((long) 2236);
		emp.setLock(true);
		eControl.lockEmployee((long)2236);
		assertThat(eControl.getEmployeeDetails((long)2236));
		
	}
	
	@Test
	@Order(7)
	public void testUnlock() {
//		Employee emp = new Employee();
		Employee emp = eControl.getEmployeeDetails((long) 2236);
		emp.setLock(false);
		eControl.unlockEmployee((long)2236);
		assertThat(eControl.getEmployeeDetails((long)2236));
		
	}


	}

	