package com.pj.hrapp.dao.impl;

import java.time.YearMonth;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.ReportDao;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;
import com.pj.hrapp.util.Queries;

@Repository
public class ReportDaoImpl implements ReportDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SSSPhilHealthReportItem> getSSSPhilHealthReportItems(YearMonth yearMonth) {
		Query query = entityManager.createNativeQuery(
				Queries.getQuery("sssPhilHealthReport"), "sssPhilHealthReportItemMapping");
		query.setParameter("month", yearMonth.getMonth().getValue());
		query.setParameter("year", yearMonth.getYear());
		return query.getResultList();
	}

}
