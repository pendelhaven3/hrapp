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
import com.pj.hrapp.model.report.SSSReport;
import com.pj.hrapp.model.report.SSSReportItem;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.service.SystemService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.ExcelUtil;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.SSSReportExcelGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

@Controller
public class SSSReportController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSSReportController.class);
    
    @Autowired private ReportService reportService;
    @Autowired private SystemService systemService;
    
    @FXML private ComboBox<Month> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private AppTableView<SSSReportItem> nonHouseholdTable;
    @FXML private AppTableView<SSSReportItem> householdTable;
    @FXML private Label totalNonHouseholdMonthlyPayField;
    @FXML private Label totalNonHouseholdEmployeeContributionField;
    @FXML private Label totalNonHouseholdEmployerContributionField;
    @FXML private Label totalNonHouseholdContributionField;
    @FXML private Label totalNonHouseholdEmployeeCompensationField;
    @FXML private Label totalNonHouseholdMonthlyPayField2;
    @FXML private Label totalNonHouseholdEmployeeContributionField2;
    @FXML private Label totalNonHouseholdEmployerContributionField2;
    @FXML private Label totalNonHouseholdContributionField2;
    @FXML private Label totalNonHouseholdEmployeeCompensationField2;
    @FXML private Label totalHouseholdMonthlyPayField;
    @FXML private Label totalHouseholdEmployeeContributionField;
    @FXML private Label totalHouseholdEmployerContributionField;
    @FXML private Label totalHouseholdContributionField;
    @FXML private Label totalHouseholdEmployeeCompensationField;
    @FXML private Label totalHouseholdMonthlyPayField2;
    @FXML private Label totalHouseholdEmployeeContributionField2;
    @FXML private Label totalHouseholdEmployerContributionField2;
    @FXML private Label totalHouseholdContributionField2;
    @FXML private Label totalHouseholdEmployeeCompensationField2;
    @FXML private Label totalMonthlyPayField;
    @FXML private Label totalEmployeeContributionField;
    @FXML private Label totalEmployerContributionField;
    @FXML private Label totalContributionField;
    @FXML private Label totalEmployeeCompensationField;
    
    private SSSReportExcelGenerator excelGenerator = new SSSReportExcelGenerator();
    
    @Override
    public void updateDisplay() {
        stageController.setTitle("SSS/PhilHealth Report");
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
        
        SSSReport report = reportService.generateSSSReport(getYearMonthCriteria());
        nonHouseholdTable.setItemsThenFocus(report.getNonHouseholdItems());
        householdTable.setItems(report.getHouseholdItems());
        if (report.isEmpty()) {
            ShowDialog.error("No records found");
        }
        
        totalNonHouseholdMonthlyPayField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdMonthlyPay()));
        totalNonHouseholdEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdEmployeeContribution()));
        totalNonHouseholdEmployerContributionField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdEmployerContribution()));
        totalNonHouseholdContributionField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdContribution()));
        totalNonHouseholdEmployeeCompensationField.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdEmployeeCompensation()));
        totalNonHouseholdMonthlyPayField2.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdMonthlyPay()));
        totalNonHouseholdEmployeeContributionField2.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdEmployeeContribution()));
        totalNonHouseholdEmployerContributionField2.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdEmployerContribution()));
        totalNonHouseholdContributionField2.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdContribution()));
        totalNonHouseholdEmployeeCompensationField2.setText(FormatterUtil.formatAmount(report.getTotalNonHouseholdEmployeeCompensation()));
        totalHouseholdMonthlyPayField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdMonthlyPay()));
        totalHouseholdEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdEmployeeContribution()));
        totalHouseholdEmployerContributionField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdEmployerContribution()));
        totalHouseholdContributionField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdContribution()));
        totalHouseholdEmployeeCompensationField.setText(FormatterUtil.formatAmount(report.getTotalHouseholdEmployeeCompensation()));
        totalHouseholdMonthlyPayField2.setText(FormatterUtil.formatAmount(report.getTotalHouseholdMonthlyPay()));
        totalHouseholdEmployeeContributionField2.setText(FormatterUtil.formatAmount(report.getTotalHouseholdEmployeeContribution()));
        totalHouseholdEmployerContributionField2.setText(FormatterUtil.formatAmount(report.getTotalHouseholdEmployerContribution()));
        totalHouseholdContributionField2.setText(FormatterUtil.formatAmount(report.getTotalHouseholdContribution()));
        totalHouseholdEmployeeCompensationField2.setText(FormatterUtil.formatAmount(report.getTotalHouseholdEmployeeCompensation()));
        totalMonthlyPayField.setText(FormatterUtil.formatAmount(report.getTotalMonthlyPay()));
        totalEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalEmployeeContribution()));
        totalEmployerContributionField.setText(FormatterUtil.formatAmount(report.getTotalEmployerContribution()));
        totalContributionField.setText(FormatterUtil.formatAmount(report.getTotalContribution()));
        totalEmployeeCompensationField.setText(FormatterUtil.formatAmount(report.getTotalEmployeeCompensation()));
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
            Workbook workbook = excelGenerator.generate(reportService.generateSSSReport(getYearMonthCriteria()),
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
        return MessageFormat.format("sss_report_{0}_{1}.xlsx", 
                String.valueOf(yearMonth.getYear()), yearMonth.getMonth().getValue());
    }
    
}
