package com.pj.hrapp.util;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;

public class SSSPhilHealthReportExcelGenerator {

	public Workbook generate(SSSPhilHealthReport report) throws IOException {
		Workbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/excel/sss_philhealth.xlsx"));
		
		Sheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		Row row = null;
		SSSPhilHealthReportItem item = null;
		
		for (int i = 0; i < report.getItems().size(); i++) {
			item = report.getItems().get(i);
			row = sheet.getRow(1 + i);
			
			cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getEmployeeName());
			
			cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getSssEmployeeContribution().doubleValue());
			
			cell = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getSssEmployerContribution().doubleValue());
			
			cell = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getSssTotalContribution().doubleValue());
			
			cell = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getMonthlyPay().doubleValue());
			
			cell = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getPagibigContribution().doubleValue());
		}
		
		return workbook;
	}

}
