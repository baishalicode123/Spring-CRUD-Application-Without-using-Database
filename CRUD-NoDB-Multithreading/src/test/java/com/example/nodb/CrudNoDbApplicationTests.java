package com.example.nodb;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.AfterClass;
import org.junit.Rule;
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
		Employee emp = new Employee();
		emp.setId(8);
		emp.setFirstname("Abcd");
		emp.setLastname("C");
		emp.setEmail("abcd@gmail.com");
		try {
		eControl.addEmployee(emp);
		}catch (InterruptedException e) {
            e.printStackTrace();
        }
		assertNotNull(eControl.getEmployeeDetails((long)8));
	}
	
	@Test
	@Order(2)
	public void testReadAll() {
		List<Employee> list = eControl.getAllEmployees();
	}
	
	@Test
	@Order(3)
	public void testOneEmployee() {
		Employee employee = eControl.getEmployeeDetails((long)8);
		assertEquals("Abcd",employee.getFirstname());
	}
	
	@Test
	@Order(4)
	public void testUpdate() {
		Employee emp = new Employee();
	    emp.setId(2005);
		emp.setFirstname("adcb");
		emp.setLastname("jk");
		emp.setEmail("hjjk@gmail.com");
		try {
			eControl.updateEmployee((long)2005,emp);
		}catch (InterruptedException f) {
			f.printStackTrace();
        }
		assertNotEquals("b",eControl.getEmployeeDetails((long)2005).getLastname());
	}
	
	@Test
	@Order(5)
	public void testDelete() throws InterruptedException {
		eControl.deleteEmployee((long)8);
		assertNull(eControl.getEmployeeDetails((long)8));
	}
	
	@Test
	@Order(6)
	public void testLock() {
		try {
		eControl.lockEmployee((long)2005);
		} catch(InterruptedException e) {
    		e.printStackTrace();
    	}
		Employee employee = eControl.getEmployeeDetails((long)2005);
		assertEquals(true,employee.isLock());
	}
	
	
	@Test
	@Order(7)
	public void testUnlock() {
		try {
		eControl.unlockEmployee((long)2005);
		} catch(InterruptedException e) {
    		e.printStackTrace();
    	}
		Employee employee=eControl.getEmployeeDetails((long)2005);
		assertEquals(false,employee.isLock());
	}
	
	@Test
	public void testUpdateWithConcurrency() throws InterruptedException {
	    int numberOfThreads = 100;
	    ExecutorService service = Executors.newFixedThreadPool(10);
	    CountDownLatch latch = new CountDownLatch(numberOfThreads);
	    Employee emp = new Employee();
	    emp.setId(2002);
		emp.setFirstname("Roy");
		emp.setLastname("Baishali");
		emp.setEmail("rbroyy@gmail.com");
	    //MyCounter counter = new MyCounter();
	    for (int i = 0; i < numberOfThreads; i++) {
	        service.execute(() -> {
	        	try {
	    		eControl.updateEmployee((long)2002 ,emp);
	        	}catch (InterruptedException f) {
	                f.printStackTrace();
	            }
	            latch.countDown();
	        });
	    }
	    latch.await();
	    assertNotEquals("roy",eControl.getEmployeeDetails((long)2002).getLastname());
	}
	
	
	@Test
	//Adding
	public void testCreateWithConcurrency() throws InterruptedException {
	    int numberOfThreads = 100;
	    ExecutorService service = Executors.newFixedThreadPool(10);
	    CountDownLatch latch = new CountDownLatch(numberOfThreads);
	    Employee emp = new Employee();
	    emp.setId(2005);
		emp.setFirstname("bai");
		emp.setLastname("shali");
		emp.setEmail("broy@gmail.com");
	    for (int i = 0; i < numberOfThreads; i++) {
	        service.execute(() -> {
	        	try {
	        		eControl.addEmployee(emp);
	        	} catch(InterruptedException e) {
	        		e.printStackTrace();
	        	}
	            latch.countDown();
	        });
	    }
	    latch.await();
	    assertNotNull(eControl.getEmployeeDetails((long)2005));
	}
	
	@Test
	//lock
	public void testLockWithConcurrency() throws InterruptedException {
	    int numberOfThreads = 100;
	    ExecutorService service = Executors.newFixedThreadPool(10);
	    CountDownLatch latch = new CountDownLatch(numberOfThreads);
	    for (int i = 0; i < numberOfThreads; i++) {
	        service.execute(() -> {
	        	try {
	        		eControl.lockEmployee((long)2002);
	        	} catch(InterruptedException e) {
	        		e.printStackTrace();
	        	}
	            latch.countDown();
	        });
	    }
	    latch.await();
	    Employee employee=eControl.getEmployeeDetails((long)2002);
	    assertEquals(true,employee.isLock());
	}
	
	@Test
	//unlock
	public void testUnlockWithConcurrency() throws InterruptedException {
	    int numberOfThreads = 100;
	    ExecutorService service = Executors.newFixedThreadPool(10);
	    CountDownLatch latch = new CountDownLatch(numberOfThreads);
	    for (int i = 0; i < numberOfThreads; i++) {
	        service.execute(() -> {
	        	try {
	        		eControl.unlockEmployee((long)200);
	        	} catch(InterruptedException e) {
	        		e.printStackTrace();
	        	}
	            latch.countDown();
	        });
	    }
	    latch.await();
	    Employee employee=eControl.getEmployeeDetails((long)2002);
	    assertEquals(false,employee.isLock());
	}
	
	
	
	
	@Rule
    public ConcurrentRule concurrently = new ConcurrentRule();
    @Rule
    public RepeatingRule rule = new RepeatingRule();
    private static EmployeeController econ = new EmployeeController();
	
    @Test
    @Concurrent(count = 10)
    @Repeating(repetition = 10)
    public void runsMultipleTimes() {
    	Employee emp = new Employee();
	    emp.setId(2002);
		emp.setFirstname("baishali");
		emp.setLastname("roy");
		emp.setEmail("broyyyy@gmail.com");
		try {
    		eControl.updateEmployee((long)2005,emp);
        	}catch (InterruptedException f) {
                f.printStackTrace();
            }
    }

    @AfterClass
    public static void annotatedTestRunsMultipleTimes() throws InterruptedException {
    	assertNotEquals("L",econ.getEmployeeDetails((long)2005).getLastname());
    }
	
	
	@Rule
  public ConcurrentRule concurrent = new ConcurrentRule();
  @Rule
  public RepeatingRule rules = new RepeatingRule();
  private static EmployeeController econn = new EmployeeController();
	
  @Test
  @Concurrent(count = 10)
  @Repeating(repetition = 10)
  public void MultipleTimes() {
  	
		try {
  		econn.lockEmployee((long)20051);
      	}catch (InterruptedException f) {
              f.printStackTrace();
          }
  }

  @AfterClass
  public static void annotatedTestRuns() throws InterruptedException {
	  Employee employee=econn.getEmployeeDetails((long)2005);
	    assertEquals(true,employee.isLock());
  }
	
  
  
  @Rule
  public ConcurrentRule concurrents = new ConcurrentRule();
  @Rule
  public RepeatingRule ruless = new RepeatingRule();
  private static EmployeeController econnn = new EmployeeController();
	
  @Test
  @Concurrent(count = 10)
  @Repeating(repetition = 10)
  public void MultipleTime() {
  	
		try {
  		econnn.unlockEmployee((long)2005);
      	}catch (InterruptedException f) {
              f.printStackTrace();
          }
  }

  @AfterClass
  public static void annotatedTestRun() throws InterruptedException {
	  Employee employee=econnn.getEmployeeDetails((long)2005);
	    assertEquals(false,employee.isLock());
  }
	
	

}