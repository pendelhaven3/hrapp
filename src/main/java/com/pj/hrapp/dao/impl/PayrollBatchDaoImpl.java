package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayrollBatchDao;
import com.pj.hrapp.model.PayrollBatch;

@Repository
public class PayrollBatchDaoImpl implements PayrollBatchDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<PayrollBatch> getAll() {
		return entityManager.createQuery("select pb from PayrollBatch pb order by pb.batchNumber desc",
				PayrollBatch.class).getResultList();
	}

	@Override
	public void save(PayrollBatch payrollBatch) {
		if (payrollBatch.getId() == null) {
			entityManager.persist(payrollBatch);
		} else {
			entityManager.merge(payrollBatch);
		}
	}

	@Override
	public PayrollBatch get(long id) {
		return entityManager.find(PayrollBatch.class, id);
	}

	@Override
	public PayrollBatch findByBatchNumber(long batchNumber) {
		TypedQuery<PayrollBatch> query = entityManager.createQuery(
				"select pb from PayrollBatch pb where pb.batchNumber = :batchNumber",
				PayrollBatch.class);
		query.setParameter("batchNumber", batchNumber);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void delete(PayrollBatch payrollBatch) {
		entityManager.remove(get(payrollBatch.getId()));
	}

}
