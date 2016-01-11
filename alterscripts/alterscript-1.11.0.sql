UPDATE employee SET payType = 'PER_DAY' WHERE paySchedule = 'WEEKLY';
UPDATE employee SET payType = 'FIXED_RATE' WHERE paySchedule = 'SEMIMONTHLY';
