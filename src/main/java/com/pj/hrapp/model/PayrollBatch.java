package com.pj.hrapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class PayrollBatch {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long batchNumber;
	private Date payDate;
	
	@Enumerated(EnumType.STRING)
	private PayPeriod payPeriod;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Pay> pays = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public List<Pay> getPays() {
		return pays;
	}

	public void setPays(List<Pay> pays) {
		this.pays = pays;
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
		PayrollBatch other = (PayrollBatch) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public PayPeriod getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(PayPeriod payPeriod) {
		this.payPeriod = payPeriod;
	}
	
}
