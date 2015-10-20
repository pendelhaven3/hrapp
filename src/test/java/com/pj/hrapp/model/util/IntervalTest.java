package com.pj.hrapp.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.pj.hrapp.util.DateUtil;

public class IntervalTest {

	private Interval interval;
	
	@Test
	public void overlap_argumentInsideInterval() {
		interval = new Interval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		Interval argument = new Interval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		
		assertEquals(argument, interval.overlap(argument));
	}
	
	@Test
	public void overlap_argumentIntersectsInterval() {
		interval = new Interval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		Interval argument = new Interval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/27/2015"));

		Interval expected = new Interval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		assertEquals(expected, interval.overlap(argument));
	}
	
	@Test
	public void overlap_argumentIntersectsEdgeOfInterval() {
		interval = new Interval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		Interval argument = new Interval(DateUtil.toDate("10/25/2015"), DateUtil.toDate("10/27/2015"));

		Interval expected = new Interval(DateUtil.toDate("10/25/2015"), DateUtil.toDate("10/25/2015"));
		assertEquals(expected, interval.overlap(argument));
	}
	
	@Test
	public void overlap_argumentDoesNotIntersectInterval() {
		interval = new Interval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		Interval argument = new Interval(DateUtil.toDate("10/26/2015"), DateUtil.toDate("10/27/2015"));

		assertNull(interval.overlap(argument));
	}
	
}
