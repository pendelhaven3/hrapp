package com.pj.hrapp.model;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.pj.hrapp.util.NumberUtil;

public class PhilHealthContributionTableTest {

	private PhilHealthContributionTable table = new PhilHealthContributionTable();

	@Before
	public void setUp() {
	    table.setFloor(new BigDecimal("10000"));
        table.setCeiling(new BigDecimal("40000"));
        table.setMultiplier(new BigDecimal("2.75"));
	}
	
	@Test
	public void salaryEqualsFloor() {
	    assertTrue(NumberUtil.equals(new BigDecimal("137.50"), table.getEmployeeShare(new BigDecimal("10000"), false)));
	}

    @Test
    public void salaryLowerThanFloor() {
        assertTrue(NumberUtil.equals(new BigDecimal("137.50"), table.getEmployeeShare(new BigDecimal("5000"), false)));
    }
    
    @Test
    public void salaryBetweenFloorAndCeiling() {
        assertTrue(NumberUtil.equals(new BigDecimal("343.75"), table.getEmployeeShare(new BigDecimal("25000"), false)));
    }
    
    @Test
    public void salaryEqualsCeiling() {
        assertTrue(NumberUtil.equals(new BigDecimal("550"), table.getEmployeeShare(new BigDecimal("40000"), false)));
    }
	
    @Test
    public void salaryMoreThanCeiling() {
        assertTrue(NumberUtil.equals(new BigDecimal("550"), table.getEmployeeShare(new BigDecimal("50000"), false)));
    }
    
}