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
		item.setRate(new BigDecimal("3100"));
		item.setNumberOfDays(31d);
		item.setPeriod(new DateInterval(DateUtil.toDate("03/01/2018"), DateUtil.toDate("03/31/2018")));
		
		assertEquals(0, item.getRate().compareTo(item.getAmount()));
	}
	
    @Test
    public void getAmount_withAbsences() {
        item.setRate(new BigDecimal("3100"));
        item.setNumberOfDays(29.5);
        item.setPeriod(new DateInterval(DateUtil.toDate("03/01/2018"), DateUtil.toDate("03/31/2018")));
        
        assertEquals(0, new BigDecimal("2950").compareTo(item.getAmount()));
    }
    
}
