package com.pj.hrapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ActiveProfiles("test")
public abstract class IntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
}
