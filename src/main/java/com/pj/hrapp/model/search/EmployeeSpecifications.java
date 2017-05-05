package com.pj.hrapp.model.search;

import org.springframework.data.jpa.domain.Specification;

import com.pj.hrapp.model.Employee;

public class EmployeeSpecifications extends BaseSpecifications {

	public static Specification<Employee> withResigned(Boolean resigned) {
		return (root, query, builder) -> builder.equal(root.get("resigned"), resigned);
	}
	
}
