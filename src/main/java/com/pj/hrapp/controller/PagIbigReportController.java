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
import com.pj.hrapp.model.report.PagIbigReport;
import com.pj.hrapp.model.report.PagIbigReportItem;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.service.SystemService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.ExcelUtil;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.PagIbigReportExcelGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

@Controller
public class PagIbigReportController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagIbigReportController.class);
    
    @Autowired private ReportService reportService;
    @Autowired private SystemService systemService;
    
    @FXML private ComboBox<Month> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private AppTableView<PagIbigReportItem> employeesTable;
    @FXML private Label totalEmployeeContributionField;
    @FXML private Label totalEmployerContributionField;
    
    private PagIbigReportExcelGenerator excelGenerator = new PagIbigReportExcelGenerator();
    
    @Override
    public void updateDisplay() {
        stageController.setTitle("Pag-IBIG Report");
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
        
        PagIbigReport report = reportService.generatePagIbigReport(getYearMonthCriteria());
        employeesTable.setItemsThenFocus(report.getItems());
        if (report.isEmpty()) {
            ShowDialog.error("No records found");
        }
        
        totalEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalEmployeeContribution()));
        totalEmployerContributionField.setText(FormatterUtil.formatAmount(report.getTotalEmployerContribution()));
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
            Workbook workbook = excelGenerator.generate(reportService.generatePagIbigReport(getYearMonthCriteria()),
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
        return MessageFormat.format("pagibig_report_{0}_{1}.xlsx", 
                String.valueOf(yearMonth.getYear()), yearMonth.getMonth().getValue());
    }
    
}
