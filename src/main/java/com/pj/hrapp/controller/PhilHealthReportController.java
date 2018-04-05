package com.pj.hrapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.report.PhilHealthReport;
import com.pj.hrapp.model.report.PhilHealthReportItem;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.service.SystemService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.ExcelUtil;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.PhilHealthReportExcelGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

@Controller
public class PhilHealthReportController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhilHealthReportController.class);
    
    @Autowired private ReportService reportService;
    @Autowired private SystemService systemService;
    
    @FXML private ComboBox<Month> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private AppTableView<PhilHealthReportItem> nonHouseholdTable;
    @FXML private AppTableView<PhilHealthReportItem> householdTable;
    @FXML private Label totalNonHouseholdMonthlyPayField;
    @FXML private Label totalNonHouseholdDueField;
    @FXML private Label totalNonHouseholdMonthlyPayField2;
    @FXML private Label totalNonHouseholdDueField2;
    @FXML private Label totalHouseholdMonthlyPayField;
    @FXML private Label totalHouseholdDueField;
    @FXML private Label totalHouseholdMonthlyPayField2;
    @FXML private Label totalHouseholdDueField2;
    @FXML private Label totalMonthlyPayField;
    @FXML private Label totalDueField;
    
    private PhilHealthReportExcelGenerator excelGenerator = new PhilHealthReportExcelGenerator();
    
    @Override
    public void updateDisplay() {
        stageController.setTitle("PhilHealth Report");
        monthComboBox.getItems().setAll(Month.values());
        yearComboBox.getItems().setAll(DateUtil.getYearDropdownValues());
        yearComboBox.setValue(Calendar.getInstance().get(Calendar.YEAR));
    }

    @FXML
    public void doOnBack() {
        stageController.back();
    }

    @FXML
    public void generateReport() {
        if (isCriteriaNotSpecified()) {
            ShowDialog.error("Month and Year must be specified");
            return;
        }
        
        PhilHealthReport report = reportService.generatePhilHealthReport(getYearMonthCriteria());
        nonHouseholdTable.setItemsThenFocus(report.getNonHouseholdItems());
        householdTable.setItems(report.getHouseholdItems());
        if (report.isEmpty()) {
            ShowDialog.error("No records found");
        }
        
        totalNonHouseholdMonthlyPayField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdMonthlyPay()));
        totalNonHouseholdDueField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdDue()));
        totalNonHouseholdMonthlyPayField2.setText(totalNonHouseholdMonthlyPayField.getText());
        totalNonHouseholdDueField2.setText(totalNonHouseholdDueField.getText());
        totalHouseholdMonthlyPayField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdMonthlyPay()));
        totalHouseholdDueField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdDue()));
        totalHouseholdMonthlyPayField2.setText(totalHouseholdMonthlyPayField.getText());
        totalHouseholdDueField2.setText(totalHouseholdDueField.getText());
        totalMonthlyPayField.setText(FormatterUtil.formatAmount(report.getTotalMonthlyPay()));
        totalDueField.setText(FormatterUtil.formatAmount(report.getTotalDue()));
    }

    private boolean isCriteriaNotSpecified() {
        return monthComboBox.getValue() == null || yearComboBox.getValue() == null;
    }

    private YearMonth getYearMonthCriteria() {
        int month = monthComboBox.getValue().getValue();
        int year = yearComboBox.getValue();
        return YearMonth.of(year, month);
    }
    
    @FXML 
    public void generateExcelReport() {
        if (isCriteriaNotSpecified()) {
            ShowDialog.error("Month and Year must be specified");
            return;
        }
        
        FileChooser fileChooser = ExcelUtil.getSaveExcelFileChooser(getExcelFilename());
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
            return;
        }
        
        try (
            Workbook workbook = excelGenerator.generate(reportService.generatePhilHealthReport(getYearMonthCriteria()),
                    systemService.getCompanyProfile());
            FileOutputStream out = new FileOutputStream(file);
        ) {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            ShowDialog.unexpectedError();
            return;
        }
        
        if (ShowDialog.confirm("Excel file generated.\nDo you wish to open the file?")) {
            ExcelUtil.openExcelFile(file);
        }
    }

    private String getExcelFilename() {
        YearMonth yearMonth = getYearMonthCriteria();
        return MessageFormat.format("philhealth_report_{0}_{1}.xlsx", 
                String.valueOf(yearMonth.getYear()), yearMonth.getMonth().getValue());
    }
    
}
