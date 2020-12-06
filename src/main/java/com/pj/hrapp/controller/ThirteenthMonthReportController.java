package com.pj.hrapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.excelgenerator.ThirteenthMonthReportExcelGenerator;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.report.ThirteenthMonthReport;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.ExcelUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.extern.slf4j.Slf4j;

@Controller
@Scope("prototype")
@Slf4j
public class ThirteenthMonthReportController extends AbstractController {

	@Autowired
	private ReportService reportService;
	
	@FXML
	private ComboBox<Integer> yearComboBox;
	
	private ThirteenthMonthReportExcelGenerator excelGenerator = new ThirteenthMonthReportExcelGenerator();
	
	@Override
	public void updateDisplay() {
        stageController.setTitle("13th Month Report");
        yearComboBox.getItems().setAll(DateUtil.getYearDropdownValues());
        yearComboBox.setValue(Calendar.getInstance().get(Calendar.YEAR));
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	private ThirteenthMonthReport doGenerateReport() {
		return reportService.generateThirteenthMonthReport(yearComboBox.getValue());
	}

	private boolean validateFields() {
		if (!isYearSpecified()) {
			ShowDialog.error("Year must be specified");
			yearComboBox.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isYearSpecified() {
		return yearComboBox.getValue() != null;
	}

	@FXML
	public void generateReport() {
		if (!validateFields()) {
			return;
		}
		
		ThirteenthMonthReport report = doGenerateReport();
		
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel files", "*.xlsx"));
        fileChooser.setInitialFileName(getExcelFilename(report));
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
        	return;
        }
		
		try (
			Workbook workbook = excelGenerator.generate(report);
			FileOutputStream out = new FileOutputStream(file);
		) {
			workbook.write(out);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		if (ShowDialog.confirm("Excel file generated.\nDo you wish to open the file?")) {
			ExcelUtil.openExcelFile(file);
		}
	}

	private String getExcelFilename(ThirteenthMonthReport report) {
		return new StringBuilder("13th month report - ").append(report.getYear()).append(".xlsx").toString();
	}
	
}
