alter table philhealthcontributiontable add householdmonthlycontribution decimal(9,0);
update philhealthcontributiontable set householdmonthlycontribution = 200;
alter table philhealthcontributiontable modify column householdmonthlycontribution decimal(9,0) not null;
