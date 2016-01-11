package com.pj.hrapp.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.pj.hrapp.model.util.DateInterval;
import com.pj.hrapp.util.DateUtil;

public class FixedRatePayslipBasicPayItemTest {

	private FixedRatePayslipBasicPayItem item;
	
	@Before
	public void setUp() {
		item = new FixedRatePayslipBasicPayItem();
	}
	
	@Test
	public void getAmount() {
		item.setRate(new BigDecimal("100"));
		item.setNumberOfDays(10d);
		item.setPeriod(new DateInterval(DateUtil.toDate("11/01/2015"), DateUtil.toDate("11/15/2015")));
		
		assertEquals(0, new BigDecimal("83.33").compareTo(item.getAmount()));
	}
	
}
