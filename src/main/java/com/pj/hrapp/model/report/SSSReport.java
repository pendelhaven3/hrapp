package com.pj.hrapp.model.report;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class SSSReport {

	private YearMonth yearMonth;
    private List<SSSReportItem> nonHouseholdItems = new ArrayList<>();
    private List<SSSReportItem> householdItems = new ArrayList<>();

    public boolean isEmpty() {
        return nonHouseholdItems.isEmpty() && householdItems.isEmpty();
    }
    
    public BigDecimal getTotalNonHouseholdMonthlyPay() {
        return nonHouseholdItems.stream()
                .map(item -> item.getMonthlyPay())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdEmployeeContribution() {
        return nonHouseholdItems.stream()
                .map(item -> item.getEmployeeContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdEmployerContribution() {
        return nonHouseholdItems.stream()
                .map(item -> item.getEmployerContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdContribution() {
        return nonHouseholdItems.stream()
                .map(item -> item.getTotalContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdEmployeeCompensation() {
        return nonHouseholdItems.stream()
                .map(item -> item.getEmployeeCompensation())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdEmployeeProvidentFundContribution() {
        return nonHouseholdItems.stream()
                .map(item -> item.getEmployeeProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdEmployerProvidentFundContribution() {
        return nonHouseholdItems.stream()
                .map(item -> item.getEmployerProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalNonHouseholdProvidentFundContribution() {
        return nonHouseholdItems.stream()
                .map(item -> item.getTotalProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdMonthlyPay() {
        return householdItems.stream()
                .map(item -> item.getMonthlyPay())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdEmployeeContribution() {
        return householdItems.stream()
                .map(item -> item.getEmployeeContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdEmployerContribution() {
        return householdItems.stream()
                .map(item -> item.getEmployerContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdContribution() {
        return householdItems.stream()
                .map(item -> item.getTotalContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdEmployeeCompensation() {
        return householdItems.stream()
                .map(item -> item.getEmployeeCompensation())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdEmployeeProvidentFundContribution() {
        return householdItems.stream()
                .map(item -> item.getEmployeeProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdEmployerProvidentFundContribution() {
        return householdItems.stream()
                .map(item -> item.getEmployerProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalHouseholdProvidentFundContribution() {
        return householdItems.stream()
                .map(item -> item.getTotalProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalMonthlyPay() {
        return getTotalNonHouseholdMonthlyPay().add(getTotalHouseholdMonthlyPay());
    }
    
    public BigDecimal getTotalEmployeeContribution() {
        return getTotalNonHouseholdEmployeeContribution().add(getTotalHouseholdEmployeeContribution());
    }
    
    public BigDecimal getTotalEmployerContribution() {
        return getTotalNonHouseholdEmployerContribution().add(getTotalHouseholdEmployerContribution());
    }
    
    public BigDecimal getTotalContribution() {
        return getTotalNonHouseholdContribution().add(getTotalHouseholdContribution());
    }
    
    public BigDecimal getTotalEmployeeCompensation() {
        return getTotalNonHouseholdEmployeeCompensation().add(getTotalHouseholdEmployeeCompensation());
    }
    
	public BigDecimal getTotalEmployeeProvidentFundContribution() {
		return getTotalNonHouseholdEmployeeProvidentFundContribution().add(getTotalHouseholdEmployeeProvidentFundContribution());
	}
    
	public BigDecimal getTotalEmployerProvidentFundContribution() {
		return getTotalNonHouseholdEmployerProvidentFundContribution().add(getTotalHouseholdEmployerProvidentFundContribution());
	}

	public BigDecimal getTotalProvidentFundContribution() {
		return getTotalEmployeeProvidentFundContribution().add(getTotalEmployerProvidentFundContribution());
	}

    public List<SSSReportItem> getNonHouseholdItems() {
        return nonHouseholdItems;
    }

    public void setNonHouseholdItems(List<SSSReportItem> nonHouseholdItems) {
        this.nonHouseholdItems = nonHouseholdItems;
    }

    public List<SSSReportItem> getHouseholdItems() {
        return householdItems;
    }

    public void setHouseholdItems(List<SSSReportItem> householdItems) {
        this.householdItems = householdItems;
    }

	public YearMonth getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(YearMonth yearMonth) {
		this.yearMonth = yearMonth;
	}

}
