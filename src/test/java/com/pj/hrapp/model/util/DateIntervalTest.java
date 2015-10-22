package com.pj.hrapp.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.pj.hrapp.util.DateUtil;

public class DateIntervalTest {

	private DateInterval interval;
	
	@Test
	public void overlap_argumentInsideInterval() {
		interval = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		
		assertEquals(argument, interval.overlap(argument));
	}
	
	@Test
	public void overlap_argumentIntersectsInterval() {
		interval = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/27/2015"));

		DateInterval expected = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		assertEquals(expected, interval.overlap(argument));
	}
	
	@Test
	public void overlap_argumentIntersectsEdgeOfInterval() {
		interval = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/25/2015"), DateUtil.toDate("10/27/2015"));

		DateInterval expected = new DateInterval(DateUtil.toDate("10/25/2015"), DateUtil.toDate("10/25/2015"));
		assertEquals(expected, interval.overlap(argument));
	}
	
	@Test
	public void overlap_argumentDoesNotIntersectInterval() {
		interval = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/26/2015"), DateUtil.toDate("10/27/2015"));

		assertNull(interval.overlap(argument));
	}
	
}
