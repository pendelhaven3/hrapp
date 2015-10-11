package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.Payroll;

@Repository
public class PayslipDaoImpl implements PayslipDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void deleteAllByPayroll(Payroll payroll) {
		Payroll updated = entityManager.find(Payroll.class, payroll.getId());
		updated.getPayslips().clear();
		
		entityManager.merge(updated);
		
		Query query = entityManager.createQuery("delete from Payslip p where p.payroll = :payroll");
		query.setParameter("payroll", payroll);
		query.executeUpdate();
	}

	@Override
	public void save(Payslip payslip) {
		if (payslip.getId() == null) {
			entityManager.persist(payslip);
		} else {
			entityManager.merge(payslip);
		}
	}

	@Override
	public List<Payslip> findAllByPayroll(Payroll payroll) {
		TypedQuery<Payslip> query = entityManager.createQuery(
				"select p from Payslip p where p.payroll = :payroll", Payslip.class);
		query.setParameter("payroll", payroll);
		return query.getResultList();
	}

	@Override
	public Payslip get(long id) {
		return entityManager.find(Payslip.class, id);
	}

	@Override
	public void delete(Payslip payslip) {
		entityManager.remove(get(payslip.getId()));
	}

}
