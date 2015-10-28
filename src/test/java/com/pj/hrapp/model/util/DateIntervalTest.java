package com.pj.hrapp.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.pj.hrapp.util.DateUtil;

public class DateIntervalTest {

	private DateInterval reference;
	
	@Test
	public void overlap_argumentInsideInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		
		assertEquals(argument, reference.overlap(argument));
		assertEquals(argument, argument.overlap(reference));
	}
	
	@Test
	public void overlap_argumentIntersectsInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/27/2015"));

		DateInterval expected = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		assertEquals(expected, reference.overlap(argument));
		assertEquals(expected, argument.overlap(reference));
	}
	
	@Test
	public void overlap_argumentIntersectsEdgeOfInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/25/2015"), DateUtil.toDate("10/27/2015"));

		DateInterval expected = new DateInterval(DateUtil.toDate("10/25/2015"), DateUtil.toDate("10/25/2015"));
		assertEquals(expected, reference.overlap(argument));
		assertEquals(expected, argument.overlap(reference));
	}
	
	@Test
	public void overlap_argumentDoesNotIntersectInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/25/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/26/2015"), DateUtil.toDate("10/27/2015"));

		assertNull(reference.overlap(argument));
		assertNull(argument.overlap(reference));
	}

	@Test
	public void overlap_dateToIsNull_argumentInsideInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), null);
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		
		assertEquals(argument, reference.overlap(argument));
		assertEquals(argument, argument.overlap(reference));
	}

	@Test
	public void overlap_dateFromIsNull_argumentInsideInterval() {
		reference = new DateInterval(null, DateUtil.toDate("10/27/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/25/2015"));
		
		assertEquals(argument, reference.overlap(argument));
		assertEquals(argument, argument.overlap(reference));
	}

	@Test
	public void overlap_dateToIsNull_argumentIntersectsInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), null);
		DateInterval argument = new DateInterval(DateUtil.toDate("10/18/2015"), DateUtil.toDate("10/23/2015"));
		
		DateInterval expected = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/23/2015"));
		assertEquals(expected, reference.overlap(argument));
		assertEquals(expected, argument.overlap(reference));
	}

	@Test
	public void overlap_dateFromIsNull_argumentIntersectsInterval() {
		reference = new DateInterval(null, DateUtil.toDate("10/20/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/18/2015"), DateUtil.toDate("10/23/2015"));
		
		DateInterval expected = new DateInterval(DateUtil.toDate("10/18/2015"), DateUtil.toDate("10/20/2015"));
		assertEquals(expected, reference.overlap(argument));
		assertEquals(expected, argument.overlap(reference));
	}

	@Test
	public void overlap_dateToIsNull_argumentIntersectsEdgeOfInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), null);
		DateInterval argument = new DateInterval(DateUtil.toDate("10/18/2015"), DateUtil.toDate("10/20/2015"));

		DateInterval expected = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/20/2015"));
		assertEquals(expected, reference.overlap(argument));
		assertEquals(expected, argument.overlap(reference));
	}
	
	@Test
	public void overlap_dateFromIsNull_argumentIntersectsEdgeOfInterval() {
		reference = new DateInterval(null, DateUtil.toDate("10/20/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/22/2015"));

		DateInterval expected = new DateInterval(DateUtil.toDate("10/20/2015"), DateUtil.toDate("10/20/2015"));
		assertEquals(expected, reference.overlap(argument));
		assertEquals(expected, argument.overlap(reference));
	}
	
	@Test
	public void overlap_dateToIsNull_argumentDoesNotIntersectInterval() {
		reference = new DateInterval(DateUtil.toDate("10/20/2015"), null);
		DateInterval argument = new DateInterval(DateUtil.toDate("10/15/2015"), DateUtil.toDate("10/18/2015"));

		assertNull(reference.overlap(argument));
		assertNull(argument.overlap(reference));
	}

	@Test
	public void overlap_dateFromIsNull_argumentDateFromIsNull() {
		reference = new DateInterval(null, DateUtil.toDate("10/20/2015"));
		DateInterval argument = new DateInterval(null, DateUtil.toDate("10/22/2015"));

		assertEquals(reference, reference.overlap(argument));
		assertEquals(reference, argument.overlap(reference));
	}
	
	@Test
	public void overlap_dateFromIsNull_argumentDateToIsNull_intersects() {
		reference = new DateInterval(null, DateUtil.toDate("10/27/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), null);

		DateInterval expected = new DateInterval(DateUtil.toDate("10/23/2015"), DateUtil.toDate("10/27/2015"));
		assertEquals(expected, reference.overlap(argument));
		assertEquals(expected, argument.overlap(reference));
	}
	
	@Test
	public void overlap_dateFromIsNull_argumentDateToIsNull_doNotIntersect() {
		reference = new DateInterval(null, DateUtil.toDate("10/21/2015"));
		DateInterval argument = new DateInterval(DateUtil.toDate("10/23/2015"), null);

		assertNull(reference.overlap(argument));
		assertNull(argument.overlap(reference));
	}
	
}
