package com.pj.hrapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.pj.hrapp.Constants;
import com.pj.hrapp.model.util.Interval;

public class DateUtil {

	public static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date toDate(String dateString) {
		try {
			return new SimpleDateFormat(Constants.DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Set<Date> generateDailyDateSet(Date startDate, Date endDate) {
		Set<Date> dates = new HashSet<>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		
		while (calendar.getTime().compareTo(endDate) <= 0) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		
		return dates;
	}
	
	public static Set<Date> generateDailyDateSet(Interval interval) {
		return generateDailyDateSet(interval.getDateFrom(), interval.getDateTo());
	}

	public static Interval getOverlappingInterval(Interval interval1, Interval interval2) {
		Set<Date> intervalDates1 = generateDailyDateSet(interval1);
		Set<Date> intervalDates2 = generateDailyDateSet(interval2);

		List<Date> overlapDates = new ArrayList<>(CollectionUtils.intersection(intervalDates1, intervalDates2));
		Collections.sort(overlapDates);
		
		return new Interval(overlapDates.get(0), overlapDates.get(overlapDates.size()-1));
	}
	
}