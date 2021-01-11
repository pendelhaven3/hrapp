package com.pj.hrapp.model.report;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class PhilHealthReport {

	private YearMonth yearMonth;
    private List<PhilHealthReportItem> nonHouseholdItems = new ArrayList<>();
    private List<PhilHealthReportItem> householdItems = new ArrayList<>();

    public boolean isEmpty() {
        return nonHouseholdItems.isEmpty() && householdItems.isEmpty();
    }
    
    public BigDecimal getTotalNonHouseholdMonthlyPay() {
        return nonHouseholdItems.stream()
                .map(item -> item.getMonthlyPay())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdDue() {
        return nonHouseholdItems.stream()
                .map(item -> item.getDue())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdMonthlyPay() {
        return householdItems.stream()
                .map(item -> item.getMonthlyPay())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdDue() {
        return householdItems.stream()
                .map(item -> item.getDue())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalMonthlyPay() {
        return getTotalNonHouseholdMonthlyPay().add(getTotalHouseholdMonthlyPay());
    }
    
    public BigDecimal getTotalDue() {
        return getTotalNonHouseholdDue().add(getTotalHouseholdDue());
    }
    
    public List<PhilHealthReportItem> getNonHouseholdItems() {
        return nonHouseholdItems;
    }

    public void setNonHouseholdItems(List<PhilHealthReportItem> nonHouseholdItems) {
        this.nonHouseholdItems = nonHouseholdItems;
    }

    public List<PhilHealthReportItem> getHouseholdItems() {
        return householdItems;
    }

    public void setHouseholdItems(List<PhilHealthReportItem> householdItems) {
        this.householdItems = householdItems;
    }

	public YearMonth getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(YearMonth yearMonth) {
		this.yearMonth = yearMonth;
	}
    
}
