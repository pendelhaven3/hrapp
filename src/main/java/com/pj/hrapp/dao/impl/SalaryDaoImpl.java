package com.pj.hrapp.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PayPeriod;
import com.pj.hrapp.model.Salary;

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
				"select s from Salary s where s.effectiveDateTo is null order by s.employee.nickname",
				Salary.class)
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
	public List<Salary> findAllCurrentByPayPeriod(PayPeriod payPeriod) {
		TypedQuery<Salary> query = entityManager.createQuery(
				"select s from Salary s where s.payPeriod = :payPeriod and s.effectiveDateTo is null", 
				Salary.class);
		query.setParameter("payPeriod", payPeriod);
		return query.getResultList();
	}

}
