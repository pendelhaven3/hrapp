package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayrollBatch;

@Repository
public class PayslipDaoImpl implements PayslipDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void deleteAllByPayrollBatch(PayrollBatch payrollBatch) {
		PayrollBatch updated = entityManager.find(PayrollBatch.class, payrollBatch.getId());
		updated.getPayslips().clear();
		
		entityManager.merge(updated);
		
		Query query = entityManager.createQuery("delete from Payslip p where p.payrollBatch = :payrollBatch");
		query.setParameter("payrollBatch", payrollBatch);
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
	public List<Payslip> findAllByPayrollBatch(PayrollBatch payrollBatch) {
		TypedQuery<Payslip> query = entityManager.createQuery(
				"select p from Payslip p where p.payrollBatch = :payrollBatch", Payslip.class);
		query.setParameter("payrollBatch", payrollBatch);
		return query.getResultList();
	}

	@Override
	public Payslip get(long id) {
		return entityManager.find(Payslip.class, id);
	}

}
