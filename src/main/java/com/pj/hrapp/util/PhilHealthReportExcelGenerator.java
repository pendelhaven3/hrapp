package com.pj.hrapp.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pj.hrapp.model.CompanyProfile;
import com.pj.hrapp.model.report.PhilHealthReport;
import com.pj.hrapp.model.report.PhilHealthReportItem;

public class PhilHealthReportExcelGenerator {

    private Row row;
    private Cell cell;
    private CellStyle numberStyle;
    private CellStyle numberBoldStyle;
    private CellStyle headerStyle;
    private CellStyle boldStyle;
    
    public Workbook generate(PhilHealthReport report, CompanyProfile companyProfile) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 256 * 30);
        sheet.setColumnWidth(1, 256 * 15);
        sheet.setColumnWidth(2, 256 * 12);
        sheet.setColumnWidth(3, 256 * 10);
        
        createStyles(workbook);
        
        row = sheet.createRow(0);
        
        cell = row.createCell(0);
        cell.setCellValue(companyProfile.getName());
        
        nextRow();
        
        cell = row.createCell(0);
        cell.setCellValue(companyProfile.getPhilhealthNumber());
        
        nextRow();
        nextRow();
        
        addHeaders();
        
        nextRow();
        
        addDataRows(report.getNonHouseholdItems());
        
        addNonHouseholdSubTotalRow(report);
        
        nextRow();
        nextRow();
        
        cell = row.createCell(0);
        cell.setCellValue("Household");
        
        nextRow();
        nextRow();
        
        addHeaders();
        
        nextRow();
        
        addDataRows(report.getHouseholdItems());

        addHouseholdSubTotalRow(report);
        
        nextRow();
        nextRow();
        
        addGrandTotalRow(report);
        
        return workbook;
    }
    
    private void createStyles(Workbook workbook) {
        numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat((short)4);
        
        headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        
        numberBoldStyle = workbook.createCellStyle();
        numberBoldStyle.setFont(boldFont);
        numberBoldStyle.setDataFormat((short)4);
        
        boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
    }

    private void addHeaders() {
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Employee");
        
        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("PhilHealth No.");
        
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Monthly Pay");
        
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Due");
    }

    private void addDataRows(List<PhilHealthReportItem> items) {
        PhilHealthReportItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            
            cell = row.createCell(0);
            cell.setCellValue(item.getEmployeeName());
            
            cell = row.createCell(1);
            cell.setCellValue(item.getPhilHealthNumber());
            
            cell = row.createCell(2);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getMonthlyPay().doubleValue());
            
            cell = row.createCell(3);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getDue().doubleValue());
            
            nextRow();
        }
    }
    
    private void addNonHouseholdSubTotalRow(PhilHealthReport report) {
        cell = row.createCell(0);
        cell.setCellValue("Subtotal");
        
        cell = row.createCell(2);
        cell.setCellStyle(numberStyle);
        cell.setCellValue(report.getTotalNonHouseholdMonthlyPay().doubleValue());
        
        cell = row.createCell(3);
        cell.setCellStyle(numberStyle);
        cell.setCellValue(report.getTotalNonHouseholdDue().doubleValue());
    }

    private void addHouseholdSubTotalRow(PhilHealthReport report) {
        cell = row.createCell(0);
        cell.setCellValue("Subtotal");
        
        cell = row.createCell(2);
        cell.setCellStyle(numberStyle);
        cell.setCellValue(report.getTotalHouseholdMonthlyPay().doubleValue());
        
        cell = row.createCell(3);
        cell.setCellStyle(numberStyle);
        cell.setCellValue(report.getTotalHouseholdDue().doubleValue());
    }
    
    private void addGrandTotalRow(PhilHealthReport report) {
        cell = row.createCell(0);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Grand Total");
        
        cell = row.createCell(2);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(report.getTotalMonthlyPay().doubleValue());
        
        cell = row.createCell(3);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(report.getTotalDue().doubleValue());
    }

    private void nextRow() {
        row = row.getSheet().createRow(row.getRowNum() + 1);
    }
    
}