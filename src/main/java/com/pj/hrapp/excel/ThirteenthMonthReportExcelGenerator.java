package com.pj.hrapp.excel;

import java.text.MessageFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pj.hrapp.model.report.MonthlyCompensation;
import com.pj.hrapp.model.report.ThirteenthMonthReport;
import com.pj.hrapp.model.report.ThirteenthMonthReportItem;

public class ThirteenthMonthReportExcelGenerator {

    private CellStyle numberStyle;
    private CellStyle numberBoldStyle;
    private CellStyle headerStyle;
    private CellStyle boldStyle;
    
    private String[] columnNames = new String[] {"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"};
	
	public Workbook generate(ThirteenthMonthReport report) {
        Workbook workbook = new XSSFWorkbook();
        
        createStyles(workbook);
        
        String pastYear = String.valueOf(report.getYear() - 1).substring(2);
        String currentYear = String.valueOf(report.getYear()).substring(2);
        
        Sheet sheet = workbook.createSheet();
		Cell cell = null;
		Row row = null;

		row = sheet.createRow(2);
		
		cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue("Period:");
		
		cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(MessageFormat.format("December {0} - November {1}", String.valueOf(report.getYear() - 1), String.valueOf(report.getYear())));
		
		row = sheet.createRow(4);
		
		cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue("Employee Description:");
		
		cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue("Business");
		
		row = sheet.createRow(6);
		
		cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("BASIC SALARY");
		
		cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Dec-" + pastYear);
		
		cell = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Jan-" + currentYear);
		
		cell = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Feb-"+ currentYear);
		
		cell = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Mar-" + currentYear);
		
		cell = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Apr-" + currentYear);
		
		cell = row.getCell(6, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("May-" + currentYear);
		
		cell = row.getCell(7, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Jun-" + currentYear);
		
		cell = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Jul-" + currentYear);
		
		cell = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Aug-" + currentYear);
		
		cell = row.getCell(10, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Sep-" + currentYear);
		
		cell = row.getCell(11, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Oct-" + currentYear);
		
		cell = row.getCell(12, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Nov-" + currentYear);
		
		cell = row.getCell(13, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("TOTAL");
		
		cell = row.getCell(14, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("13TH");
		
		int i = 0;
		int rowNumber = 8;
		for (ThirteenthMonthReportItem item : report.getItems()) {
			rowNumber = 8 + (i*2);
			row = sheet.getRow(rowNumber);
			if (row == null) {
				row = sheet.createRow(rowNumber);
			}
			
			cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getEmployee().getFirstName() + " " + item.getEmployee().getLastName());
			
			int j = 0;
			for (MonthlyCompensation compensation : item.getMonthlyCompensations()) {
				cell = row.getCell(1 + j, Row.CREATE_NULL_AS_BLANK);
				cell.setCellStyle(numberStyle);
				cell.setCellValue(compensation.getCompensation().doubleValue());
				j++;
			}
			
			cell = row.getCell(13, Row.CREATE_NULL_AS_BLANK);
            cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellStyle(numberStyle);
            cell.setCellFormula(MessageFormat.format("SUM(B{0}:M{0})", rowNumber+1));
			
			cell = row.getCell(14, Row.CREATE_NULL_AS_BLANK);
            cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellStyle(numberStyle);
            cell.setCellFormula(MessageFormat.format("N{0}/12", rowNumber+1));
			
			i++;
		}
		
		row = sheet.createRow(rowNumber + 2);
		
		cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue("Grand Total");
		
		for (int k = 0; k < columnNames.length; k++) {
			cell = row.getCell(1 + k, Row.CREATE_NULL_AS_BLANK);
	        cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellStyle(numberStyle);
	        cell.setCellFormula(MessageFormat.format("SUM({0}9:{0}{1})", columnNames[k], rowNumber+1));
		}
		
		sheet.autoSizeColumn(0);
		
		int monthColumnSize = 11;
		for (int k = 1; k <= 14; k++) {
			sheet.setColumnWidth(k, 256 * monthColumnSize);
		}
		
		return workbook;
	}
	
    private void createStyles(Workbook workbook) {
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        
        numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat((short)4);
        
        headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setFont(boldFont);
        
        numberBoldStyle = workbook.createCellStyle();
        numberBoldStyle.setFont(boldFont);
        numberBoldStyle.setDataFormat((short)4);
        
        boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
    }
	
}
