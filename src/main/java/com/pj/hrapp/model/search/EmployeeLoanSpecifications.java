package com.pj.hrapp.model.search;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;

public class EmployeeLoanSpecifications extends BaseSpecifications {

	public static Specification<EmployeeLoan> withEmployee(Employee employee) {
		return (root, query, builder) -> builder.equal(root.get("employee"), employee);
	}
	
	public static Specification<EmployeeLoan> withPaid(Boolean paid) {
		return (root, query, builder) -> builder.equal(root.get("paid"), paid);
	}

	public static Specification<EmployeeLoan> withPaymentDate(Date paymentDate) {
		return (root, query, builder) -> builder.or(
				builder.isNull(root.get("paymentStartDate")),
				builder.lessThanOrEqualTo(root.get("paymentStartDate"), paymentDate));
	}
	
}
