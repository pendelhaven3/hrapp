package com.pj.hrapp.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class PhilHealthContributionTableEntryTest {

	private PhilHealthContributionTableEntry entry;
	
	@Before
	public void setUp() {
		entry = new PhilHealthContributionTableEntry();
	}
	
	@Test
	public void getSalaryRangeAsString() {
		entry.setSalaryFrom(new BigDecimal("9000"));
		entry.setSalaryTo(new BigDecimal("9999.99"));
		
		assertEquals("9,000.00 - 9,999.99", entry.getSalaryRangeAsString());
	}

	@Test
	public void getSalaryRangeAsString_salaryFromNotSpecified() {
		entry.setSalaryTo(new BigDecimal("9999.99"));
		
		assertEquals("9,999.99 and below", entry.getSalaryRangeAsString());
	}

	@Test
	public void getSalaryRangeAsString_salaryToNotSpecified() {
		entry.setSalaryFrom(new BigDecimal("9000"));
		
		assertEquals("9,000.00 and up", entry.getSalaryRangeAsString());
	}
	
}
