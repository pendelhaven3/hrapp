CREATE TABLE PhilHealthContributionTable (
	id BIGINT(20) PRIMARY KEY,
	floor DECIMAL(9, 2) NOT NULL,
	ceiling DECIMAL(9, 2) NOT NULL,
	multiplier DECIMAL(9, 2) NOT NULL);

INSERT INTO PhilHealthContributionTable (id, floor, ceiling, multiplier) VALUES (1, 10000, 40000, 2.75);
