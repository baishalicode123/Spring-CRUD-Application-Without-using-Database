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
	public void testCreate() throws InterruptedException {
		Employee emp = new Employee();
		emp.setId(2001);
		emp.setFirstname("royy");
		emp.setLastname("B");
		emp.setEmail("royb@gmail.com");
		try {
		eControl.addEmployee(emp);
		}catch (InterruptedException e) {
            e.printStackTrace();
        }
		assertNotNull(eControl.getEmployeeDetails((long)2001));
	}
	
	@Test
	@Order(2)
	public void testReadAll() {
		List<Employee> list = eControl.getAllEmployees();
	}
	
	@Test
	@Order(3)
	public void testOneEmployee() throws InterruptedException {
		Employee employee = eControl.getEmployeeDetails((long)8);
		assertEquals("Abcd",employee.getFirstname());
	}
	
	@Test
	@Order(4)
	public void testUpdate() throws InterruptedException {
		Employee emp = new Employee();
	    emp.setId(2001);
		emp.setFirstname("ROY");
		emp.setLastname("Baishalii");
		emp.setEmail("rb@gmail.com");
		try {
			eControl.updateEmployee((long)2001,emp);
		}catch (InterruptedException f) {
			f.printStackTrace();
        }
		assertNotEquals("Baishali",eControl.getEmployeeDetails((long)2001).getLastname());
	}
	
	@Test
	@Order(5)
	public void testDelete() throws InterruptedException {
		eControl.deleteEmployee((long)2001);
		assertNull(eControl.getEmployeeDetails((long)2001));
	}
	
	@Test
	@Order(6)
	public void testLock() throws InterruptedException {
		try {
		eControl.lockEmployee((long)2001);
		} catch(InterruptedException e) {
    		e.printStackTrace();
    	}
		Employee employee = eControl.getEmployeeDetails((long)2001);
		assertEquals(true,employee.isLock());
	}
	
	
	@Test
	@Order(7)
	public void testUnlock() throws InterruptedException {
		try {
		eControl.unlockEmployee((long)2001);
		} catch(InterruptedException e) {
    		e.printStackTrace();
    	}
		Employee employee=eControl.getEmployeeDetails((long)2001);
		assertEquals(false,employee.isLock());
	}
	
	@Test
	public void testUpdateWithConcurrency() throws InterruptedException {
	    int numberOfThreads = 100;
	    ExecutorService service = Executors.newFixedThreadPool(10);
	    CountDownLatch latch = new CountDownLatch(numberOfThreads);
	    Employee emp = new Employee();
	    emp.setId(1);
		emp.setFirstname("Roy");
		emp.setLastname("broy");
		emp.setEmail("rbroy@gmail.com");
	    //MyCounter counter = new MyCounter();
	    for (int i = 0; i < numberOfThreads; i++) {
	        service.execute(() -> {
	        	try {
	    		eControl.updateEmployee((long)2001 ,emp);
	        	}catch (InterruptedException f) {
	                f.printStackTrace();
	            }
	            latch.countDown();
	        });
	    }
	    latch.await();
	    assertNotEquals("m",eControl.getEmployeeDetails((long)2001).getLastname());
	}
	
	
	@Test
	//Adding
	public void testCreateWithConcurrency() throws InterruptedException {
	    int numberOfThreads = 100;
	    ExecutorService service = Executors.newFixedThreadPool(10);
	    CountDownLatch latch = new CountDownLatch(numberOfThreads);
	    Employee emp = new Employee();
	    emp.setId(200);
		emp.setFirstname("roy");
		emp.setLastname("Baishali");
		emp.setEmail("rb@gmail.com");
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
	    assertNotNull(eControl.getEmployeeDetails((long)2001));
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
	        		eControl.lockEmployee((long)2001);
	        	} catch(InterruptedException e) {
	        		e.printStackTrace();
	        	}
	            latch.countDown();
	        });
	    }
	    latch.await();
	    Employee employee=eControl.getEmployeeDetails((long)2001);
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
	        		eControl.unlockEmployee((long)2001);
	        	} catch(InterruptedException e) {
	        		e.printStackTrace();
	        	}
	            latch.countDown();
	        });
	    }
	    latch.await();
	    Employee employee=eControl.getEmployeeDetails((long)2001);
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
	    emp.setId(2001);
		emp.setFirstname("bai");
		emp.setLastname("shali");
		emp.setEmail("bai@gmail.com");
		try {
    		eControl.updateEmployee((long)2001,emp);
        	}catch (InterruptedException f) {
                f.printStackTrace();
            }
    }

    @AfterClass
    public static void annotatedTestRunsMultipleTimes() throws InterruptedException {
    	assertNotEquals("L",econ.getEmployeeDetails((long)2001).getLastname());
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
  		econn.lockEmployee((long)1);
      	}catch (InterruptedException f) {
              f.printStackTrace();
          }
  }

  @AfterClass
  public static void annotatedTestRuns() throws InterruptedException {
	  Employee employee=econn.getEmployeeDetails((long)2001);
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
  		econnn.unlockEmployee((long)2001);
      	}catch (InterruptedException f) {
              f.printStackTrace();
          }
  }

  @AfterClass
  public static void annotatedTestRun() throws InterruptedException {
	  Employee employee=econnn.getEmployeeDetails((long)2001);
	    assertEquals(false,employee.isLock());
  }
	
	

}