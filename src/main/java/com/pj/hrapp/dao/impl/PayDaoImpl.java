package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayDao;
import com.pj.hrapp.model.Pay;
import com.pj.hrapp.model.PayrollBatch;

@Repository
public class PayDaoImpl implements PayDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void deleteAllByPayrollBatch(PayrollBatch payrollBatch) {
		Query query = entityManager.createQuery("delete from Pay p where p.payrollBatch = :payrollBatch");
		query.setParameter("payrollBatch", payrollBatch);
		query.executeUpdate();
	}

	@Override
	public void save(Pay pay) {
		if (pay.getId() == null) {
			entityManager.persist(pay);
		} else {
			entityManager.merge(pay);
		}
	}

	@Override
	public List<Pay> findAllByPayrollBatch(PayrollBatch payrollBatch) {
		TypedQuery<Pay> query = entityManager.createQuery(
				"select p from Pay p where p.payrollBatch = :payrollBatch", Pay.class);
		query.setParameter("payrollBatch", payrollBatch);
		return query.getResultList();
	}

}
