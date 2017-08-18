package com.pj.hrapp;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@RunWith(SpringJUnit4WithJavaFXClassRunner.class)
@SpringApplicationConfiguration(classes = HRApp.class)
@ActiveProfiles(value = {"test"})
public abstract class IntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
}
