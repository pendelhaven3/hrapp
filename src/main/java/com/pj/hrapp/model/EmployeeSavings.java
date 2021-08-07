package com.pj.hrapp.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EmployeeSavings {

	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	private Employee employee;
	
	private BigDecimal savedAmountPerPayday;
	private BigDecimal currentBalance = BigDecimal.ZERO;
	
	public void deposit(BigDecimal amount) {
		currentBalance = currentBalance.add(amount);
	}

	public boolean hasEnoughBalance(BigDecimal amount) {
		return currentBalance.compareTo(amount) >= 0;
	}

	public void withdraw(BigDecimal amount) {
		currentBalance = currentBalance.subtract(amount);
	}
	
}
