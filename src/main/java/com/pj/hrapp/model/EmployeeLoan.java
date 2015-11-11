package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.pj.hrapp.util.FormatterUtil;

@Entity
public class EmployeeLoan {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Employee employee;

	@Column(columnDefinition = "date")
	private Date loanDate;
	
	private BigDecimal amount;
	private Integer numberOfPayments;
	private BigDecimal paymentAmount;
	private String remarks;

	@OneToMany(mappedBy = "loan", cascade = CascadeType.REMOVE)
	private List<EmployeeLoanPayment> payments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(Integer numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<EmployeeLoanPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<EmployeeLoanPayment> payments) {
		this.payments = payments;
	}

	public Date getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
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
		EmployeeLoan other = (EmployeeLoan) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public Integer getNextPaymentNumber() {
		return payments.stream()
				.map(payment -> payment.getPaymentNumber())
				.max((o1, o2) -> o1.compareTo(o2))
				.orElseGet(() -> 0) + 1;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Loan [").append(FormatterUtil.formatDate(loanDate)).append("]")
				.toString();
	}
	
}
