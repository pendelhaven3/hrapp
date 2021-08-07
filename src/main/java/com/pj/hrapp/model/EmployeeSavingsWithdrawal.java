package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EmployeeSavingsWithdrawal {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private EmployeeSavings savings;
	
	private BigDecimal amount;
	
	private Date withdrawalDate;
	
}
