package com.pj.hrapp.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.model.PayrollBatch;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.Payslip.BasicPayBreakdownItem;
import com.pj.hrapp.service.ExcelService;
import com.pj.hrapp.service.PayrollService;

@Service
public class ExcelServiceImpl implements ExcelService {

	@Autowired private PayrollService payrollService;
	
	private String[][] cellNames;
	
	@PostConstruct
	private void generateCellNames() {
		int rows = 50;
		int columns = 8;
		String[] columnNames = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};
		
		cellNames = new String[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cellNames[i][j] = columnNames[j] + String.valueOf(i);
			}
		}
	}
	
	private int[] payslipRows = new int[] {0, 0, 0, 17, 17, 17, 34, 34, 34};
	
	private int[][] payslipColumns = new int[][] {
		{0, 1, 2},
		{3, 4, 5},
		{6, 7, 8},
		{0, 1, 2},
		{3, 4, 5},
		{6, 7, 8},
		{0, 1, 2},
		{3, 4, 5},
		{6, 7, 8}
	};
	
	@Override
	public XSSFWorkbook generate(PayrollBatch payrollBatch) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/excel/payslip.xlsx"));
		CellStyle amountCellStyle = createAmountCellStyle(workbook);
		
		Sheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		Row row = null;
		
		List<Payslip> payslips = payrollBatch.getPayslips();
		for (int i = 0; i < payslips.size(); i++) {
			Payslip payslip = payrollService.getPayslip(payslips.get(i).getId());
			int currentRow = payslipRows[i];
			
			cell = sheet.getRow(currentRow).getCell(payslipColumns[i][0]);
			cell.setCellValue(payslip.getEmployee().getFirstAndLastName());
			
			currentRow++;
			
			cell = sheet.getRow(currentRow).getCell(payslipColumns[i][0]);
			cell.setCellValue(payslip.getPayrollBatch().getPayDate());
			
			currentRow += 2;

			List<BasicPayBreakdownItem> items = payslip.getBasicPayItems();
			for (int j = 0; j < items.size(); j++) {
				BasicPayBreakdownItem item = items.get(j);
				row = sheet.getRow(currentRow);
				
				cell = row.getCell(payslipColumns[i][0]);
				cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(item.getRate().doubleValue());
				cell.setCellStyle(amountCellStyle);
				
				cell = row.getCell(payslipColumns[i][1]);
				cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(item.getNumberOfDays());
				
				cell = row.getCell(payslipColumns[i][2]);
				cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(new StringBuilder()
						.append(cellNames[row.getRowNum() + 1][payslipColumns[i][0]])
						.append("*")
						.append(cellNames[row.getRowNum() + 1][payslipColumns[i][1]])
						.toString());
				
				currentRow++;
			}
		}
		
		XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		return workbook;
	}

	private CellStyle createAmountCellStyle(XSSFWorkbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat((short)BuiltinFormats.getBuiltinFormat("#,##0.00"));
		return cellStyle;
	}
	
}
