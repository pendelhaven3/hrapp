update payslip
set payType = 'FIXED_RATE'
where payroll_id in (
	select id
	from payroll
	where paySchedule = 'SEMIMONTHLY'
	and batchNumber < 152
	order by batchNumber desc
);
