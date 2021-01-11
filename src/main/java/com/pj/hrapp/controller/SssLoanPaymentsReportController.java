package com.pj.hrapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.EmployeeLoanType;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.ExcelUtil;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.SSSLoanPaymentsReportExcelGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

@Controller
public class SssLoanPaymentsReportController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SssLoanPaymentsReportController.class);
    
    @Autowired private ReportService reportService;
    
    @FXML private ComboBox<Month> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private AppTableView<EmployeeLoanPayment> loanPaymentsTable;
    @FXML private Label totalAmortizationsLabel;
    
    private SSSLoanPaymentsReportExcelGenerator excelGenerator = new SSSLoanPaymentsReportExcelGenerator();
    
    @Override
    public void updateDisplay() {
        stageController.setTitle("SSS Loan Payments Report");
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
        
        YearMonth yearMonth = getYearMonthCriteria();
        List<EmployeeLoanPayment> items = reportService.generateEmployeeLoanPaymentsReport(
                DateUtil.toDate(yearMonth.atDay(1)), DateUtil.toDate(yearMonth.atEndOfMonth()), EmployeeLoanType.SSS);
        Collections.sort(items, (o1, o2) -> o1.getLoan().getEmployee().getFullName().compareTo(o2.getLoan().getEmployee().getFullName()));
        loanPaymentsTable.setItems(items);
        if (items.isEmpty()) {
            ShowDialog.error("No records found");
        }
        totalAmortizationsLabel.setText(FormatterUtil.formatAmount(
                items.stream().map(item -> item.getAmount()).reduce(BigDecimal.ZERO, (x,y) -> x.add(y))));
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
        
        YearMonth yearMonth = getYearMonthCriteria();
        List<EmployeeLoanPayment> items = reportService.generateEmployeeLoanPaymentsReport(
                DateUtil.toDate(yearMonth.atDay(1)), DateUtil.toDate(yearMonth.atEndOfMonth()), EmployeeLoanType.SSS);
        Collections.sort(items, (o1, o2) -> o1.getLoan().getEmployee().getFullName().compareTo(o2.getLoan().getEmployee().getFullName()));
        
        try (
            Workbook workbook = excelGenerator.generate(items, yearMonth);
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
        return MessageFormat.format("sss_loan_payments_{0}_{1}.xlsx", 
                String.valueOf(yearMonth.getYear()), yearMonth.getMonth().getValue());
    }
    
}
