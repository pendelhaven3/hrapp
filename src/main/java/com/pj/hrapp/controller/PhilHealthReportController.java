package com.pj.hrapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    @FXML private Label totalCompanyEmployeeContributionField;
    @FXML private Label totalCompanyEmployerContributionField;
    @FXML private Label totalCompanyContributionField;
    @FXML private Label totalHouseholdEmployeeContributionField;
    @FXML private Label totalHouseholdEmployerContributionField;
    @FXML private Label totalHouseholdContributionField;
    @FXML private Label totalCompanyEmployeeContributionField2;
    @FXML private Label totalCompanyEmployerContributionField2;
    @FXML private Label totalCompanyContributionField2;
    @FXML private Label totalHouseholdEmployeeContributionField2;
    @FXML private Label totalHouseholdEmployerContributionField2;
    @FXML private Label totalHouseholdContributionField2;
    @FXML private Label totalEmployeeContributionField;
    @FXML private Label totalEmployerContributionField;
    @FXML private Label totalContributionField;
    
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
        
        totalCompanyEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdDue()));
        totalCompanyEmployerContributionField.setText(totalCompanyEmployeeContributionField.getText());
        totalCompanyContributionField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdDue().multiply(BigDecimal.valueOf(2L))));
        totalHouseholdEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdDue()));
        totalHouseholdEmployerContributionField.setText(totalHouseholdEmployeeContributionField.getText());
        totalHouseholdContributionField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdDue().multiply(BigDecimal.valueOf(2L))));
        totalCompanyEmployeeContributionField2.setText(totalCompanyEmployeeContributionField.getText());
        totalCompanyEmployerContributionField2.setText(totalCompanyEmployerContributionField.getText());
        totalCompanyContributionField2.setText(totalCompanyContributionField.getText());
        totalHouseholdEmployeeContributionField2.setText(totalHouseholdEmployeeContributionField.getText());
        totalHouseholdEmployerContributionField2.setText(totalHouseholdEmployerContributionField.getText());
        totalHouseholdContributionField2.setText(totalHouseholdContributionField.getText());
        totalEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalDue()));
        totalEmployerContributionField.setText(totalEmployeeContributionField.getText());
        totalContributionField.setText(FormatterUtil.formatAmount(report.getTotalDue().multiply(BigDecimal.valueOf(2L))));
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
