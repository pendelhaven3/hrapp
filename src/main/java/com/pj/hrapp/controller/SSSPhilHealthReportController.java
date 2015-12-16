package com.pj.hrapp.controller;

import java.time.Month;
import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SSSPhilHealthReportController extends AbstractController {

	@Autowired private ReportService reportService;
	
	@FXML private ComboBox<Month> monthComboBox;
	@FXML private ComboBox<Integer> yearComboBox;
	@FXML private AppTableView<SSSPhilHealthReportItem> reportTable;
	
	@Override
	public void updateDisplay() {
		monthComboBox.getItems().setAll(Month.values());
		yearComboBox.getItems().setAll(DateUtil.getYearDropdownValues());
	}

	@FXML 
	public void doOnBack() {
		stageController.showReportListScreen();
	}

	@FXML 
	public void generateReport() {
		SSSPhilHealthReport report = reportService.generateSSSPhilHealthReport(getYearMonthCriteria());
		reportTable.setItemsThenFocus(report.getItems());
		if (report.getItems().isEmpty()) {
			ShowDialog.error("No records found");
		}
	}

	private YearMonth getYearMonthCriteria() {
		int month = monthComboBox.getValue().getValue();
		int year = yearComboBox.getValue();
		return YearMonth.of(year, month);
	}

}
