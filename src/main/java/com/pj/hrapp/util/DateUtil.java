package com.pj.hrapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

import com.pj.hrapp.Constants;
import com.pj.hrapp.model.util.DateInterval;

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
	
	public static Set<Date> generateDailyDateSet(DateInterval interval) {
		return generateDailyDateSet(interval.getDateFrom(), interval.getDateTo());
	}

	public static int getMonth(Date date) {
		return DateUtils.toCalendar(date).get(Calendar.MONTH);
	}

	public static DateInterval generateMonthYearInterval(YearMonth yearMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, yearMonth.getYear());
		calendar.set(Calendar.MONTH, yearMonth.getMonthValue() - 1);
		calendar.set(Calendar.DATE, 1);
		Date startDate = calendar.getTime();
		
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date endDate = calendar.getTime();
		
		return new DateInterval(startDate, endDate);
	}
	
	public static Date getFirstDateOfMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		
		return calendar.getTime();
	}
	
	public static YearMonth getYearMonth(Date date) {
		return YearMonth.from(DateUtil.toLocalDate(date));
	}

	public static boolean isSunday(Date date) {
		return DateUtils.toCalendar(date).get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}

	public static Date max(Date date1, Date date2) {
		return date1.compareTo(date2) >= 0 ? date1 : date2;
	}

	public static Date min(Date date1, Date date2) {
		return date1.compareTo(date2) <= 0 ? date1 : date2;
	}
	
}