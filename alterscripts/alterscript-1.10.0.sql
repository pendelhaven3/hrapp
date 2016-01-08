ALTER TABLE payroll DROP includeSSSPagibigPhilhealth;

UPDATE payroll SET periodCoveredFrom = '2015-11-02', periodCoveredTo = '2015-11-09' WHERE batchNumber = 1;
UPDATE payroll SET periodCoveredFrom = '2015-11-09', periodCoveredTo = '2015-11-14' WHERE batchNumber = 2;
UPDATE payroll SET periodCoveredFrom = '2015-11-01', periodCoveredTo = '2015-11-15' WHERE batchNumber = 3;
UPDATE payroll SET periodCoveredFrom = '2015-11-16', periodCoveredTo = '2015-11-21' WHERE batchNumber = 4;
UPDATE payroll SET periodCoveredFrom = '2015-11-23', periodCoveredTo = '2015-11-28' WHERE batchNumber = 5;
UPDATE payroll SET periodCoveredFrom = '2015-11-16', periodCoveredTo = '2015-11-30' WHERE batchNumber = 6;
UPDATE payroll SET periodCoveredFrom = '2015-11-30', periodCoveredTo = '2015-12-05' WHERE batchNumber = 7;
UPDATE payroll SET periodCoveredFrom = '2015-12-07', periodCoveredTo = '2015-12-12' WHERE batchNumber = 8;
UPDATE payroll SET periodCoveredFrom = '2015-12-01', periodCoveredTo = '2015-12-15' WHERE batchNumber = 9;
UPDATE payroll SET periodCoveredFrom = '2015-12-14', periodCoveredTo = '2015-12-19' WHERE batchNumber = 10;
UPDATE payroll SET periodCoveredFrom = '2015-12-21', periodCoveredTo = '2015-12-24' WHERE batchNumber = 11;
UPDATE payroll SET periodCoveredFrom = '2015-12-16', periodCoveredTo = '2015-12-30' WHERE batchNumber = 12;
UPDATE payroll SET periodCoveredFrom = '2015-12-28', periodCoveredTo = '2015-12-30' WHERE batchNumber = 13;

