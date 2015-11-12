ALTER TABLE employee MODIFY COLUMN payschedule VARCHAR(20);
UPDATE employee SET payschedule = 'WEEKLY' where payschedule = '0';
UPDATE employee SET payschedule = 'SEMIMONTHLY' where payschedule = '1';