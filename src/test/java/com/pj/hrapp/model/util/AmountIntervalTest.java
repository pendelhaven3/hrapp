package com.pj.hrapp.model.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class AmountIntervalTest {

	private AmountInterval interval;
	
	@Test
	public void overlapsWith_otherInsideInterval() {
		interval = new AmountInterval(new BigDecimal("1000"), new BigDecimal("1500"));
		AmountInterval other = new AmountInterval(new BigDecimal("1100"), new BigDecimal("1400"));
		
		assertTrue(interval.overlapsWith(other));
		assertTrue(other.overlapsWith(interval));
	}
	
	@Test
	public void overlapsWith_otherIntersectsInterval() {
		interval = new AmountInterval(new BigDecimal("1000"), new BigDecimal("1500"));
		AmountInterval other = new AmountInterval(new BigDecimal("1200"), new BigDecimal("1600"));
		
		assertTrue(interval.overlapsWith(other));
		assertTrue(other.overlapsWith(interval));
	}
	
	@Test
	public void overlapsWith_otherIntersectsEdgeOfInterval() {
		interval = new AmountInterval(new BigDecimal("1000"), new BigDecimal("1500"));
		AmountInterval other = new AmountInterval(new BigDecimal("1500"), new BigDecimal("1600"));
		
		assertTrue(interval.overlapsWith(other));
		assertTrue(other.overlapsWith(interval));
	}
	
	@Test
	public void overlapsWith_otherDoesNotIntersectInterval() {
		interval = new AmountInterval(new BigDecimal("1000"), new BigDecimal("1500"));
		AmountInterval other = new AmountInterval(new BigDecimal("1600"), new BigDecimal("1700"));
		
		assertFalse(interval.overlapsWith(other));
		assertFalse(other.overlapsWith(interval));
	}
	
	@Test
	public void overlapsWith_noAmountTo_otherInsideInterval() {
		interval = new AmountInterval(new BigDecimal("1000"), null);
		AmountInterval other = new AmountInterval(new BigDecimal("1100"), new BigDecimal("1400"));
		
		assertTrue(interval.overlapsWith(other));
		assertTrue(other.overlapsWith(interval));
	}
	
}
