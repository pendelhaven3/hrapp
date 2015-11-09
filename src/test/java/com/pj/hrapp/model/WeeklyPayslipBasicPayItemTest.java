package com.pj.hrapp.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class WeeklyPayslipBasicPayItemTest {

	private WeeklyPayslipBasicPayItem item;
	
	@Before
	public void setUp() {
		item = new WeeklyPayslipBasicPayItem();
	}
	
	@Test
	public void getAmount() {
		item.setRate(new BigDecimal("100"));
		item.setNumberOfDays(5d);
		
		assertEquals(0, new BigDecimal("500").compareTo(item.getAmount()));
	}
	
}
