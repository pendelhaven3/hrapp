package com.pj.hrapp.model.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;

/**
 * Represents a period between two dates
 * 
 * @author PJ Miranda
 *
 */
public class DateInterval {

	private Date dateFrom;
	private Date dateTo;

	public DateInterval(Date dateFrom, Date dateTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	public DateInterval overlap(DateInterval interval) {
		List<Date> overlapDates = new ArrayList<>(CollectionUtils.intersection(
				toDateList(), interval.toDateList()));
		if (!overlapDates.isEmpty()) {
			Collections.sort(overlapDates);
			return new DateInterval(overlapDates.get(0), overlapDates.get(overlapDates.size() - 1));
		} else {
			return null;
		}
	}
	
	public List<Date> toDateList() {
		List<Date> dates = new ArrayList<>();
		Calendar calendar = DateUtils.toCalendar(dateFrom);
		
		while (calendar.getTime().compareTo(dateTo) <= 0) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		
		return dates;
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(dateFrom)
				.append(dateTo)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateInterval other = (DateInterval) obj;
		return new EqualsBuilder()
				.append(dateFrom, other.getDateFrom())
				.append(dateTo, other.getDateTo())
				.isEquals();
	}

	public boolean contains(Date date) {
		return dateFrom.compareTo(date) <= 0 && date.compareTo(dateTo) <= 0;
	}

}
