package com.pj.hrapp.service;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pj.hrapp.model.Payroll;

public interface ExcelService {

	XSSFWorkbook generate(Payroll payroll) throws IOException ;
	
}
