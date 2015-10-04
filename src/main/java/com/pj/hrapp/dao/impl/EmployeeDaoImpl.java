package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.EmployeeDao;
import com.pj.hrapp.model.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Employee> getAll() {
		return entityManager.createQuery("select e from Employee e order by e.nickname", Employee.class)
				.getResultList();
	}

	@Override
	public void save(Employee employee) {
		if (employee.getId() == null) {
			entityManager.persist(employee);
		} else {
			entityManager.merge(employee);
		}
	}

	@Override
	public Employee get(long id) {
		return entityManager.find(Employee.class, id);
	}

	@Override
	public Employee findByEmployeeNumber(long employeeNumber) {
		TypedQuery<Employee> query = 
				entityManager.createQuery("select e from Employee e where e.employeeNumber = :employeeNumber", 
						Employee.class);
		query.setParameter("employeeNumber", employeeNumber);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void delete(Employee employee) {
		entityManager.remove(get(employee.getId()));
	}

}
