package com.pj.hrapp.model.util;

import java.math.BigDecimal;

public class AmountInterval {

	private BigDecimal amountFrom;
	private BigDecimal amountTo;

	public AmountInterval(BigDecimal amountFrom, BigDecimal amountTo) {
		this.amountFrom = amountFrom;
		this.amountTo = amountTo;
	}
	
	public boolean overlapsWith(AmountInterval other) {
		return (amountFrom.compareTo(other.getAmountFrom()) <= 0 
				&& (amountTo == null || other.getAmountFrom().compareTo(amountTo) <= 0))
				|| (other.getAmountFrom().compareTo(amountFrom) <= 0 
				&& (other.getAmountTo() == null || other.getAmountTo().compareTo(amountFrom) >= 0));
	}

	public BigDecimal getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(BigDecimal amountFrom) {
		this.amountFrom = amountFrom;
	}

	public BigDecimal getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(BigDecimal amountTo) {
		this.amountTo = amountTo;
	}

}
