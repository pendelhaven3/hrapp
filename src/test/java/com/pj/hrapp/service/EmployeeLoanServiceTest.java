package com.pj.hrapp.service;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.pj.hrapp.dao.EmployeeLoanRepository;
import com.pj.hrapp.exception.EmployeeAlreadyResignedException;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.service.impl.EmployeeLoanServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeLoanServiceTest {

	private EmployeeLoanService service;
	
	@Mock private EmployeeLoanRepository employeeLoanRepository;
	
	@Before
	public void setUp() {
		service = new EmployeeLoanServiceImpl();
		ReflectionTestUtils.setField(service, "employeeLoanRepository", employeeLoanRepository);
	}
	
	@Test
	public void save_newLoan_success() {
		EmployeeLoan loan = properNewLoan();
		
		service.save(loan);
		
		verify(employeeLoanRepository).save(loan);
	}
	
	@Test
	public void save_existingLoan_success() {
		EmployeeLoan loan = properExistingLoan();
		
		service.save(loan);
		
		verify(employeeLoanRepository).save(loan);
	}
	
	@Test(expected = EmployeeAlreadyResignedException.class)
	public void save_cannotCreateNewLoanForResignedEmployee() {
		service.save(loanWithResignedEmployee());
	}

	private EmployeeLoan properNewLoan() {
		EmployeeLoan loan = new EmployeeLoan();
		loan.setEmployee(new Employee());
		
		return loan;
	}

	private EmployeeLoan properExistingLoan() {
		EmployeeLoan loan = new EmployeeLoan();
		loan.setId(1L);
		loan.setEmployee(new Employee());
		
		return loan;
	}

	private EmployeeLoan loanWithResignedEmployee() {
		EmployeeLoan loan = new EmployeeLoan();
		loan.setEmployee(resignedEmployee());
		
		return loan;
	}

	private Employee resignedEmployee() {
		Employee employee = new Employee();
		employee.setResigned(true);
		
		return employee;
	}
	
}
