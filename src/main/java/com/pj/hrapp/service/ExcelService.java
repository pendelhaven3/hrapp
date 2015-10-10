package com.pj.hrapp.service;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pj.hrapp.model.PayrollBatch;

public interface ExcelService {

	XSSFWorkbook generate(PayrollBatch payrollBatch) throws IOException ;
	
}
