CREATE TABLE calendar (
  date DATE PRIMARY KEY
);

ALTER TABLE salary ADD payType VARCHAR(20);
ALTER TABLE salary ADD paySchedule VARCHAR(20);

UPDATE salary a
JOIN employee b
	ON b.id = a.employee_id
SET a.payType = b.payType, a.paySchedule = b.paySchedule
WHERE a.effectiveDateTo IS NULL
AND b.resigned = false;

UPDATE payslip a
JOIN salary b
	ON b.employee_id = a.employee_id
JOIN payroll c
	ON c.id = a.payroll_id
SET a.payType = b.payType, a.paySchedule = b.paySchedule
WHERE b.effectiveDateTo IS NULL
AND c.batchNumber = 178;
