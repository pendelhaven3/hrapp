package com.pj.hrapp;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class OnStartup {
    
    private ApplicationContext context;
    
    public OnStartup(ApplicationContext context) {
        this.context = context;
    }

    public void fire() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(context.getBean(DataSource.class));
        jdbcTemplate.update("alter table ssscontributiontableentry modify compensationFrom decimal(19,2) null");
    }
    
}
