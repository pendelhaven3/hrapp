package com.pj.hrapp.model.util;

import java.util.Date;

import org.joda.time.DateTime;

/**
 * Represents a period between two dates
 * 
 * @author PJ Miranda
 *
 */
public class Interval {

	private Date dateFrom;
	private Date dateTo;

	public Interval(Date dateFrom, Date dateTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	public Interval(org.joda.time.Interval interval) {
		this.dateFrom = new Date(interval.getStartMillis());
		this.dateTo = new Date(interval.getEndMillis());
	}
	
	public org.joda.time.Interval toJodaInterval() {
		return new org.joda.time.Interval(
				new DateTime(dateFrom.getTime()),
				new DateTime(dateTo.getTime()));
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

}
