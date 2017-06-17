package com.pj.hrapp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppDatePicker;
import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class EmployeeLoanPaymentsReportController extends AbstractController {

	@Autowired private ReportService reportService;
	
	@FXML private AppDatePicker fromDateDatePicker;
	@FXML private AppDatePicker toDateDatePicker;
	@FXML private AppTableView<EmployeeLoanPayment> reportTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Loan Payments Report");
	}

	@FXML 
	public void doOnBack() {
		stageController.showReportListScreen();
	}

	@FXML 
	public void generateReport() {
		Date from = DateUtil.toDate(fromDateDatePicker.getValue());
		Date to = DateUtil.toDate(toDateDatePicker.getValue());
		
		if (from == null || to == null) {
			ShowDialog.error("From Date and To Date must be specified");
			return;
		}
		
		List<EmployeeLoanPayment> payments = reportService.generateEmployeeLoanPaymentsReport(from, to);
		reportTable.setItemsThenFocus(payments);
		if (payments.isEmpty()) {
			ShowDialog.error("No records found");
		}
	}

}
