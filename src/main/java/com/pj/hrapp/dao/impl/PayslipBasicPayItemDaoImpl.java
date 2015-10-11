package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayslipBasicPayItemDao;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipBasicPayItem;

@Repository
public class PayslipBasicPayItemDaoImpl implements PayslipBasicPayItemDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(PayslipBasicPayItem payslipBasicPay) {
		if (payslipBasicPay.getId() == null) {
			entityManager.persist(payslipBasicPay);
		} else {
			entityManager.merge(payslipBasicPay);
		}
	}

	@Override
	public List<PayslipBasicPayItem> findAllByPayslip(Payslip payslip) {
		TypedQuery<PayslipBasicPayItem> query = entityManager.createQuery(
				"select i from PayslipBasicPayItem i where i.payslip = :payslip",
				PayslipBasicPayItem.class);
		query.setParameter("payslip", payslip);
		return query.getResultList();
	}
	
}
