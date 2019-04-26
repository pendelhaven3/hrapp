package com.pj.hrapp.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.pj.hrapp.model.report.SSSPhilHealthReportItem;
import com.pj.hrapp.model.util.AmountInterval;
import com.pj.hrapp.util.FormatterUtil;

import lombok.Getter;
import lombok.Setter;

// TODO: Find better place for this!
@SqlResultSetMapping(name = "sssPhilHealthReportItemMapping",
	classes = {
	    @ConstructorResult(targetClass = SSSPhilHealthReportItem.class, columns = {
	        @ColumnResult(name = "employeeFullName"),
	        @ColumnResult(name = "employeeNickname"),
	        @ColumnResult(name = "sssEmployeeContribution"),
	        @ColumnResult(name = "sssEmployerContribution"),
	        @ColumnResult(name = "monthlyPay"),
	        @ColumnResult(name = "pagibigContribution"),
            @ColumnResult(name = "sssNumber"),
            @ColumnResult(name = "philHealthNumber")
	    })
	}
)

@Entity
@Getter
@Setter
public class SSSContributionTableEntry {

	@Id
	@GeneratedValue
	private Long id;
	
	private BigDecimal compensationFrom;
	private BigDecimal compensationTo;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
    private BigDecimal employeeCompensation;
	
    private boolean household;
    
	public AmountInterval getCompensationRange() {
		return new AmountInterval(compensationFrom, compensationTo);
	}
	
	public boolean isCompensationToSpecified() {
		return compensationTo != null;
	}

	public boolean overlapsWith(SSSContributionTableEntry other) {
		if (equals(other)) {
			return false;
		} else {
			return getCompensationRange().overlapsWith(other.getCompensationRange());
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
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
		SSSContributionTableEntry other = (SSSContributionTableEntry) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}
	
	public String getCompensationRangeAsString() {
	    if (compensationFrom == null) {
	        return new StringBuilder().append("below ").append(FormatterUtil.formatAmount(compensationTo)).toString();
	    } else if (compensationTo == null) {
            return new StringBuilder().append(FormatterUtil.formatAmount(compensationFrom)).append(" and above").toString();
	    } else {
	        return new StringBuilder()
	                .append(FormatterUtil.formatAmount(compensationFrom))
	                .append(" - ")
	                .append(FormatterUtil.formatAmount(compensationTo))
	                .toString();
	    }
	    
	}
	
	public boolean contains(BigDecimal compensation) {
		return compensationFrom.compareTo(compensation) <= 0 &&
				(compensationTo == null || compensation.compareTo(compensationTo) <= 0);
	}

}
