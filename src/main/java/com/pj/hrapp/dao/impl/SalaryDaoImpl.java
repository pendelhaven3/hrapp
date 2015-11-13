package com.pj.hrapp.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;

@Repository
public class SalaryDaoImpl implements SalaryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(Salary salary) {
		if (salary.getId() == null) {
			entityManager.persist(salary);
		} else {
			entityManager.merge(salary);
		}
	}

	@Override
	public Salary get(long id) {
		return entityManager.find(Salary.class, id);
	}

	@Override
	public List<Salary> getAllCurrent() {
		return entityManager.createQuery(
				"select s from Salary s where s.effectiveDateTo is null"
				+ " order by s.employee.firstName, s.employee.lastName", Salary.class)
				.getResultList();
	}

	@Override
	public Salary findByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate) {
		TypedQuery<Salary> query = entityManager.createQuery(
				"select s from Salary s where s.employee = :employee and s.effectiveDateFrom <= :effectiveDate"
				+ " and (s.effectiveDateTo is null or s.effectiveDateTo > :effectiveDate)", Salary.class);
		query.setParameter("employee", employee);
		query.setParameter("effectiveDate", effectiveDate);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void delete(Salary salary) {
		entityManager.remove(get(salary.getId()));
	}

	@Override
	public List<Salary> findAllCurrentByPaySchedule(PaySchedule paySchedule) {
		TypedQuery<Salary> query = entityManager.createQuery(
				"select s from Salary s where s.paySchedule= :paySchedule and s.effectiveDateTo is null", 
				Salary.class);
		query.setParameter("paySchedule", paySchedule);
		return query.getResultList();
	}

	@Override
	public List<Salary> search(SalarySearchCriteria criteria) {
		Map<String, Object> paramMap = new HashMap<>();
		
		StringBuilder sql = new StringBuilder("select s from Salary s where 1 = 1");
		if (criteria.getEmployee() != null) {
			sql.append(" and s.employee = :employee");
			paramMap.put("employee", criteria.getEmployee());
		}
		if (criteria.getEffectiveDateFrom() != null) {
			sql.append(" and (")
				.append("s.effectiveDateFrom >= :effectiveDateFrom")
				.append(" or (s.effectiveDateFrom <= :effectiveDateFrom and (s.effectiveDateTo is null or s.effectiveDateTo >= :effectiveDateFrom))")
				.append(")");
			paramMap.put("effectiveDateFrom", criteria.getEffectiveDateFrom());
		}
		if (criteria.getEffectiveDateTo() != null) {
			sql.append(" and (")
				.append("s.effectiveDateTo <= :effectiveDateTo")
				.append(" or (s.effectiveDateFrom <= :effectiveDateTo and (s.effectiveDateTo is null or s.effectiveDateTo >= :effectiveDateTo))")
				.append(")");
			paramMap.put("effectiveDateTo", criteria.getEffectiveDateTo());
		}

		TypedQuery<Salary> query = entityManager.createQuery(sql.toString(), Salary.class);
		for (String key : paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		return query.getResultList();
	}

	@Override
	public Salary findByEmployee(Employee employee) {
		TypedQuery<Salary> query = 
				entityManager.createQuery("select s from Salary s where s.employee = :employee", Salary.class);
		query.setParameter("employee", employee);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
