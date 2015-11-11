package com.pj.hrapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.dialog.AddPayslipDialog;
import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.ExcelService;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayrollController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PayrollController.class);
	
	@Autowired private PayrollService payrollService;
	@Autowired private ExcelService excelService;
	
	@Autowired(required = false)
	private AddPayslipDialog addPayslipDialog;
	
	@FXML private Label batchNumberLabel;
	@FXML private Label payDateLabel;
	@FXML private Label payScheduleLabel;
	@FXML private Label includeSSSPagibigPhilhealthLabel;
	@FXML private TableView<Payslip> payslipsTable;
	
	@Parameter private Payroll payroll;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Payroll");
		
		payroll = payrollService.getPayroll(payroll.getId());
		for (int i = 0; i < payroll.getPayslips().size(); i++) {
			payroll.getPayslips().set(i, payrollService.getPayslip(payroll.getPayslips().get(i).getId()));
		}
		
		batchNumberLabel.setText(payroll.getBatchNumber().toString());
		payDateLabel.setText(FormatterUtil.formatDate(payroll.getPayDate()));
		payScheduleLabel.setText(payroll.getPaySchedule().toString());
		includeSSSPagibigPhilhealthLabel.setText(
				payroll.isIncludeSSSPagibigPhilhealth() ? "Yes" : "No");
		
		payslipsTable.getItems().setAll(payroll.getPayslips());
		payslipsTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				openSelectedPayslip();
			}
		});
	}

	protected void openSelectedPayslip() {
		stageController.showPayslipScreen(payslipsTable.getSelectionModel().getSelectedItem());
	}

	@FXML public void doOnBack() {
		stageController.showPayrollListScreen();
	}

	@FXML public void updatePayroll() {
		stageController.showUpdatePayrollScreen(payroll);
	}

	@FXML public void deletePayroll() {
		if (ShowDialog.confirm("Delete payroll?")) {
			try {
				payrollService.delete(payroll);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
			}
			
			ShowDialog.info("Payroll deleted");
			stageController.showPayrollListScreen();
		}
	}

	@FXML public void generateExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel files", "*.xlsx"));
        fileChooser.setInitialFileName(getExcelFilename());
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
        	return;
        }
		
		try (
			XSSFWorkbook workbook = excelService.generate(payroll);
			FileOutputStream out = new FileOutputStream(file);
		) {
			workbook.write(out);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		if (ShowDialog.confirm("Excel file generated.\nDo you wish to open the file?")) {
			openExcelFile(file);
		}
	}

	private void openExcelFile(File file) {
		String[] cmdarray = new String[]{"cmd.exe", "/c", file.getAbsolutePath()}; 
		try {
			Runtime.getRuntime().exec(cmdarray);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
		} 	
	}

	private String getExcelFilename() {
		return new StringBuilder()
				.append("PAYSLIP as of ")
				.append(new SimpleDateFormat("MM-dd").format(payroll.getPayDate()))
				.append(".xlsx")
				.toString();
	}

	@FXML public void deletePayslip() {
		if (!isPayslipSelected()) {
			ShowDialog.error("No payslip selected");
			return;
		}
		
		if (ShowDialog.confirm("Delete selected payslip?")) {
			try {
				payrollService.delete(getSelectedPayslip());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Payslip deleted");
			updateDisplay();
		}
	}

	private boolean isPayslipSelected() {
		return getSelectedPayslip() != null;
	}

	private Payslip getSelectedPayslip() {
		return payslipsTable.getSelectionModel().getSelectedItem();
	}

	@FXML
	public void addPayslip() {
		Map<String, Object> model = new HashMap<>();
		model.put("payroll", payroll);
		
		addPayslipDialog.showAndWait(model);
		
		updateDisplay();
	}
	
}
