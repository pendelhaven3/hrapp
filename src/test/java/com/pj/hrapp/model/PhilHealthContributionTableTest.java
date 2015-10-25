package com.pj.hrapp.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class PhilHealthContributionTableTest {

	private PhilHealthContributionTable table;

	@Test
	public void isComplete_falseIfNoEntries() {
		table = new PhilHealthContributionTable(Collections.emptyList());
		
		assertFalse(table.isComplete());
	}

	@Test
	public void isComplete_trueWithOneProperEntry() {
		PhilHealthContributionTableEntry entry = new PhilHealthContributionTableEntry();
		
		table = new PhilHealthContributionTable(Arrays.asList(entry));
		
		assertTrue(table.isComplete());
	}

	@Test
	public void isComplete_trueWithTwoProperEntries() {
		PhilHealthContributionTableEntry entry = new PhilHealthContributionTableEntry();
		entry.setSalaryTo(new BigDecimal("8999.99"));
		
		PhilHealthContributionTableEntry entry2 = new PhilHealthContributionTableEntry();
		entry2.setSalaryFrom(new BigDecimal("9000"));
		
		table = new PhilHealthContributionTable(Arrays.asList(entry, entry2));
		
		assertTrue(table.isComplete());
	}

	@Test
	public void isComplete_trueWithThreeProperEntries() {
		PhilHealthContributionTableEntry entry = new PhilHealthContributionTableEntry();
		entry.setSalaryTo(new BigDecimal("8999.99"));
		
		PhilHealthContributionTableEntry entry2 = new PhilHealthContributionTableEntry();
		entry2.setSalaryFrom(new BigDecimal("9000"));
		entry2.setSalaryTo(new BigDecimal("9999.99"));
		
		PhilHealthContributionTableEntry entry3 = new PhilHealthContributionTableEntry();
		entry3.setSalaryFrom(new BigDecimal("10000"));
		
		table = new PhilHealthContributionTable(Arrays.asList(entry, entry2, entry3));
		
		assertTrue(table.isComplete());
	}
	
	@Test
	public void isComplete_falseWithLowerEndOfCompensationRangeNotCovered() {
		PhilHealthContributionTableEntry entry = new PhilHealthContributionTableEntry();
		entry.setSalaryFrom(new BigDecimal("9000"));
		entry.setSalaryTo(new BigDecimal("9999.99"));
		
		PhilHealthContributionTableEntry entry2 = new PhilHealthContributionTableEntry();
		entry2.setSalaryFrom(new BigDecimal("10000"));
		
		table = new PhilHealthContributionTable(Arrays.asList(entry, entry2));
		
		assertFalse(table.isComplete());
	}

	@Test
	public void isComplete_falseWithHigherEndOfCompensationRangeNotCovered() {
		PhilHealthContributionTableEntry entry = new PhilHealthContributionTableEntry();
		entry.setSalaryTo(new BigDecimal("8999.99"));
		
		PhilHealthContributionTableEntry entry2 = new PhilHealthContributionTableEntry();
		entry2.setSalaryFrom(new BigDecimal("9000"));
		entry2.setSalaryTo(new BigDecimal("9999.99"));
		
		table = new PhilHealthContributionTable(Arrays.asList(entry, entry2));
		
		assertFalse(table.isComplete());
	}
	
	@Test
	public void isComplete_falseWithMiddlePartOfCompensationRangeNotCovered() {
		PhilHealthContributionTableEntry entry = new PhilHealthContributionTableEntry();
		entry.setSalaryTo(new BigDecimal("8999.99"));
		
		PhilHealthContributionTableEntry entry2 = new PhilHealthContributionTableEntry();
		entry2.setSalaryFrom(new BigDecimal("10000"));
		
		table = new PhilHealthContributionTable(Arrays.asList(entry, entry2));
		
		assertFalse(table.isComplete());
	}

	@Test
	public void isComplete_falseWithMiddleEntryWithNoSalaryToSpecified() {
		PhilHealthContributionTableEntry entry = new PhilHealthContributionTableEntry();
		entry.setSalaryTo(new BigDecimal("8999.99"));
		
		PhilHealthContributionTableEntry entry2 = new PhilHealthContributionTableEntry();
		entry2.setSalaryFrom(new BigDecimal("9000"));
		
		PhilHealthContributionTableEntry entry3 = new PhilHealthContributionTableEntry();
		entry3.setSalaryFrom(new BigDecimal("10000"));
		
		table = new PhilHealthContributionTable(Arrays.asList(entry, entry2, entry3));
		
		assertFalse(table.isComplete());
	}
	
}
