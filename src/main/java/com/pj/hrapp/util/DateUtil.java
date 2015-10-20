package com.pj.hrapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

}