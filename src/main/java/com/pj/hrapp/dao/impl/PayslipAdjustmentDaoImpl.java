package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayslipAdjustmentDao;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;

@Repository
public class PayslipAdjustmentDaoImpl implements PayslipAdjustmentDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(PayslipAdjustment payslipAdjustment) {
		if (payslipAdjustment.getId() == null) {
			entityManager.persist(payslipAdjustment);
		} else {
			entityManager.merge(payslipAdjustment);
		}
	}

	@Override
	public List<PayslipAdjustment> findAllByPayslip(Payslip payslip) {
		TypedQuery<PayslipAdjustment> query = entityManager.createQuery(
				"select pa from PayslipAdjustment pa where pa.payslip = :payslip", PayslipAdjustment.class);
		query.setParameter("payslip", payslip);
		return query.getResultList();
	}

	@Override
	public void delete(PayslipAdjustment payslipAdjustment) {
		entityManager.remove(get(payslipAdjustment.getId()));
	}
	
	@Override
	public PayslipAdjustment get(long id) {
		return entityManager.find(PayslipAdjustment.class, id);
	}
	
}
