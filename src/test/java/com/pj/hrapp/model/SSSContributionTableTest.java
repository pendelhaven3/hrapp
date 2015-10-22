package com.pj.hrapp.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.pj.hrapp.model.SSSContributionTable;
import com.pj.hrapp.model.SSSContributionTableEntry;

public class SSSContributionTableTest {

	private SSSContributionTable table;

	@Test
	public void isComplete_falseIfNoEntries() {
		table = new SSSContributionTable(Collections.emptyList());
		
		assertFalse(table.isComplete());
	}
	
	@Test
	public void isComplete_trueWithOneProperEntry() {
		SSSContributionTableEntry entry = new SSSContributionTableEntry();
		entry.setCompensationFrom(SSSContributionTable.MINIMUM_COMPENSATION);
		
		table = new SSSContributionTable(Arrays.asList(entry));
		
		assertTrue(table.isComplete());
	}

	@Test
	public void isComplete_trueWithTwoProperEntries() {
		SSSContributionTableEntry entry = new SSSContributionTableEntry();
		entry.setCompensationFrom(SSSContributionTable.MINIMUM_COMPENSATION);
		entry.setCompensationTo(new BigDecimal("1249.99"));
		
		SSSContributionTableEntry entry2 = new SSSContributionTableEntry();
		entry2.setCompensationFrom(new BigDecimal("1250"));
		
		table = new SSSContributionTable(Arrays.asList(entry, entry2));
		
		assertTrue(table.isComplete());
	}

	@Test
	public void isComplete_trueWithThreeProperEntries() {
		SSSContributionTableEntry entry = new SSSContributionTableEntry();
		entry.setCompensationFrom(SSSContributionTable.MINIMUM_COMPENSATION);
		entry.setCompensationTo(new BigDecimal("1249.99"));
		
		SSSContributionTableEntry entry2 = new SSSContributionTableEntry();
		entry2.setCompensationFrom(new BigDecimal("1250"));
		entry2.setCompensationTo(new BigDecimal("1749.99"));
		
		SSSContributionTableEntry entry3 = new SSSContributionTableEntry();
		entry3.setCompensationFrom(new BigDecimal("1750"));
		
		table = new SSSContributionTable(Arrays.asList(entry, entry2, entry3));
		
		assertTrue(table.isComplete());
	}
	
	@Test
	public void isComplete_falseWithLowerEndOfCompensationRangeNotCovered() {
		SSSContributionTableEntry entry = new SSSContributionTableEntry();
		entry.setCompensationFrom(new BigDecimal("1250"));
		entry.setCompensationTo(new BigDecimal("1749.99"));
		
		SSSContributionTableEntry entry2 = new SSSContributionTableEntry();
		entry2.setCompensationFrom(new BigDecimal("1750"));
		
		table = new SSSContributionTable(Arrays.asList(entry, entry2));
		
		assertFalse(table.isComplete());
	}

	@Test
	public void isComplete_falseWithHigherEndOfCompensationRangeNotCovered() {
		SSSContributionTableEntry entry = new SSSContributionTableEntry();
		entry.setCompensationFrom(SSSContributionTable.MINIMUM_COMPENSATION);
		entry.setCompensationTo(new BigDecimal("1249.99"));
		
		SSSContributionTableEntry entry2 = new SSSContributionTableEntry();
		entry2.setCompensationFrom(new BigDecimal("1250"));
		entry2.setCompensationTo(new BigDecimal("1749.99"));
		
		table = new SSSContributionTable(Arrays.asList(entry, entry2));
		
		assertFalse(table.isComplete());
	}
	
	@Test
	public void isComplete_falseWithMiddlePartOfCompensationRangeNotCovered() {
		SSSContributionTableEntry entry = new SSSContributionTableEntry();
		entry.setCompensationFrom(SSSContributionTable.MINIMUM_COMPENSATION);
		entry.setCompensationTo(new BigDecimal("1249.99"));
		
		SSSContributionTableEntry entry2 = new SSSContributionTableEntry();
		entry2.setCompensationFrom(new BigDecimal("1750"));
		
		table = new SSSContributionTable(Arrays.asList(entry, entry2));
		
		assertFalse(table.isComplete());
	}

	@Test
	public void isComplete_falseWithMiddleEntryWithNoCompensationToSpecified() {
		SSSContributionTableEntry entry = new SSSContributionTableEntry();
		entry.setCompensationFrom(SSSContributionTable.MINIMUM_COMPENSATION);
		entry.setCompensationTo(new BigDecimal("1249.99"));
		
		SSSContributionTableEntry entry2 = new SSSContributionTableEntry();
		entry2.setCompensationFrom(new BigDecimal("1250"));
		
		SSSContributionTableEntry entry3 = new SSSContributionTableEntry();
		entry3.setCompensationFrom(new BigDecimal("1750"));
		
		table = new SSSContributionTable(Arrays.asList(entry, entry2, entry3));
		
		assertFalse(table.isComplete());
	}
	
}
