package com.pj.hrapp.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class SSSContributionTableEntryTest {

	private SSSContributionTableEntry entry;
	
	@Before
	public void setUp() {
		entry = new SSSContributionTableEntry();
		entry.setId(1L);
	}

	@Test
	public void overlapsWith_falseIfSameId() {
		entry.setCompensationFrom(new BigDecimal("1000"));
		entry.setCompensationTo(new BigDecimal("1500"));
		
		SSSContributionTableEntry other = new SSSContributionTableEntry();
		other.setId(entry.getId());
		other.setCompensationFrom(new BigDecimal("1000"));
		other.setCompensationTo(new BigDecimal("1500"));
		
		assertFalse(entry.overlapsWith(other));
	}
	
	@Test
	public void overlapsWith_otherInsideInterval() {
		entry.setCompensationFrom(new BigDecimal("1000"));
		entry.setCompensationTo(new BigDecimal("1500"));
		
		SSSContributionTableEntry other = new SSSContributionTableEntry();
		other.setCompensationFrom(new BigDecimal("1100"));
		other.setCompensationTo(new BigDecimal("1400"));
		
		assertTrue(entry.overlapsWith(other));
		assertTrue(other.overlapsWith(entry));
	}
	
	@Test
	public void overlapsWith_otherIntersectsInterval() {
		entry.setCompensationFrom(new BigDecimal("1000"));
		entry.setCompensationTo(new BigDecimal("1500"));
		
		SSSContributionTableEntry other = new SSSContributionTableEntry();
		other.setCompensationFrom(new BigDecimal("1200"));
		other.setCompensationTo(new BigDecimal("1600"));
		
		assertTrue(entry.overlapsWith(other));
		assertTrue(other.overlapsWith(entry));
	}
	
	@Test
	public void overlapsWith_otherIntersectsEdgeOfInterval() {
		entry.setCompensationFrom(new BigDecimal("1000"));
		entry.setCompensationTo(new BigDecimal("1500"));
		
		SSSContributionTableEntry other = new SSSContributionTableEntry();
		other.setCompensationFrom(new BigDecimal("1500"));
		other.setCompensationTo(new BigDecimal("1700"));
		
		assertTrue(entry.overlapsWith(other));
		assertTrue(other.overlapsWith(entry));
	}
	
	@Test
	public void overlapsWith_otherDoesNotIntersectInterval() {
		entry.setCompensationFrom(new BigDecimal("1000"));
		entry.setCompensationTo(new BigDecimal("1500"));
		
		SSSContributionTableEntry other = new SSSContributionTableEntry();
		other.setCompensationFrom(new BigDecimal("1600"));
		other.setCompensationTo(new BigDecimal("1700"));
		
		assertFalse(entry.overlapsWith(other));
		assertFalse(other.overlapsWith(entry));
	}
	
	@Test
	public void overlapsWith_noCompensationTo_otherInsideInterval() {
		entry.setCompensationFrom(new BigDecimal("1000"));
		
		SSSContributionTableEntry other = new SSSContributionTableEntry();
		other.setCompensationFrom(new BigDecimal("1500"));
		other.setCompensationTo(new BigDecimal("1700"));
		
		assertTrue(entry.overlapsWith(other));
		assertTrue(other.overlapsWith(entry));
	}
	
}
