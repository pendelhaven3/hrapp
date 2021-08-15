package com.pj.hrapp.onstartup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.pj.hrapp.model.EmployeeLoanType;
import com.pj.hrapp.service.EmployeeLoanService;

@Component
public class InitializeLoanTypes implements ApplicationRunner {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private EmployeeLoanService employeeLoanService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		int loanTypeCount = jdbcTemplate.queryForObject("select count(1) from employeeloantype", Integer.class);
		if (loanTypeCount > 0) {
			return;
		}

		List<String> loanTypes = jdbcTemplate.queryForList("select description from employeeloan where description is not null group by description",
				String.class);
		for (String loanType : loanTypes) {
			EmployeeLoanType loanTypeObject = new EmployeeLoanType();
			loanTypeObject.setDescription(loanType);
			employeeLoanService.save(loanTypeObject);
		}
		jdbcTemplate.update("update employeeloan set loantype_id = (select id from employeeloantype where employeeloantype.description = employeeloan.description) where description is not null");
	}

}
