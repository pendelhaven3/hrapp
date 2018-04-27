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

import com.pj.hrapp.model.EmployeeLoanPayment;

public class PagIbigLoanPaymentsReportExcelGenerator {

    private Row row;
    private Cell cell;
    private CellStyle numberStyle;
    private CellStyle numberBoldStyle;
    private CellStyle headerStyle;
    private CellStyle boldStyle;
    private CellStyle dateStyle;
    
    public Workbook generate(List<EmployeeLoanPayment> items) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 256 * 18);
        sheet.setColumnWidth(1, 256 * 30);
        sheet.setColumnWidth(2, 256 * 15);
        sheet.setColumnWidth(3, 256 * 15);
        sheet.setColumnWidth(4, 256 * 15);
        
        createStyles(workbook);
        
        row = sheet.createRow(0);
        
        addHeaders();
        
        nextRow();
        
        addDataRows(items);
        
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
        
        dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("mm/dd/yyyy"));
    }

    private void addHeaders() {
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Pag-IBIG No.");
        
        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Employee");
        
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Amortization");
        
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("TIN");
        
        cell = row.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Date of Birth");
    }

    private void addDataRows(List<EmployeeLoanPayment> items) {
        for (EmployeeLoanPayment item : items) {
            cell = row.createCell(0);
            cell.setCellValue(item.getLoan().getEmployee().getPagibigNumber());
            
            cell = row.createCell(1);
            cell.setCellValue(item.getLoan().getEmployee().getFullName());
            
            cell = row.createCell(2);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getLoan().getPaymentAmount().doubleValue());
            
            cell = row.createCell(3);
            cell.setCellValue(item.getLoan().getEmployee().getTin());
            
            cell = row.createCell(4);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(item.getLoan().getEmployee().getBirthday());
            
            nextRow();
        }
    }
    
    private void nextRow() {
        row = row.getSheet().createRow(row.getRowNum() + 1);
    }
    
}