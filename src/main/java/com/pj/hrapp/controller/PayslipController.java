package com.pj.hrapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.dialog.AddPayslipLoanPaymentDialog;
import com.pj.hrapp.dialog.AddValeProductDialog;
import com.pj.hrapp.dialog.EmployeeAttendanceDialog;
import com.pj.hrapp.dialog.PayslipAdjustmentDialog;
import com.pj.hrapp.exception.ConnectToMagicException;
import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipBasicPayItem;
import com.pj.hrapp.model.PreviewPayslipItem;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.service.EmployeeLoanService;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.service.ValeProductService;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
public class PayslipController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PayslipController.class);
	
	@Autowired private PayrollService payrollService;
	@Autowired private EmployeeService employeeService;
	@Autowired private ValeProductService valeProductService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@Autowired(required = false)
	private EmployeeAttendanceDialog employeeAttendanceDialog;
	
	@Autowired(required = false)
	private AddValeProductDialog addValeProductDialog;
	
	@Autowired(required = false)
	private PayslipAdjustmentDialog payslipAdjustmentDialog;
	
	@Autowired(required = false)
	private AddPayslipLoanPaymentDialog addPayslipLoanPaymentDialog;
	
	@FXML private Label payrollBatchNumberLabel;
	@FXML private Label employeeLabel;
	@FXML private Label periodCoveredFromLabel;
	@FXML private Label periodCoveredToLabel;
	@FXML private Label basicPayLabel;
	@FXML private Label adjustmentsLabel;
	@FXML private Label netPayLabel;
	@FXML private TableView<EmployeeAttendance> attendancesTable;
	@FXML private TableView<PayslipBasicPayItem> basicPayItemsTable;
	@FXML private AppTableView<EmployeeLoanPayment> loanPaymentsTable;
	@FXML private AppTableView<ValeProduct> valeProductsTable;
	@FXML private AppTableView<PayslipAdjustment> adjustmentsTable;
	@FXML private TableView<PreviewPayslipItem> previewPayslipTable;
	@FXML private TabPane tabPane;

	@Parameter private Payslip payslip;

	@Override
	public void updateDisplay() {
		stageController.setTitle("Payslip");
		
		payslip = payrollService.getPayslip(payslip.getId());
		payrollBatchNumberLabel.setText(payslip.getPayroll().getBatchNumber().toString());
		employeeLabel.setText(payslip.getEmployee().toString());
		periodCoveredFromLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredFrom()));
		periodCoveredToLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredTo()));
		basicPayLabel.setText(FormatterUtil.formatAmount(payslip.getBasicPay()));
		adjustmentsLabel.setText(FormatterUtil.formatAmount(payslip.getTotalPayslipAdjustments()));
		netPayLabel.setText(FormatterUtil.formatAmount(payslip.getNetPay()));
		
		basicPayItemsTable.getItems().setAll(payslip.getBasicPayItems());
		
		attendancesTable.getItems().setAll(payslip.getAttendances());
		attendancesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				editSelectedAttendance();
			}
		});

		loanPaymentsTable.setItems(payslip.getLoanPayments());
		loanPaymentsTable.setDeleteKeyAction(() -> deleteLoanPayment());
		
		valeProductsTable.getItems().setAll(payslip.getValeProducts());
		valeProductsTable.setDeleteKeyAction(() -> deleteValeProduct());
		
		adjustmentsTable.getItems().setAll(payslip.getAdjustments());
		adjustmentsTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				editSelectedAdjustment();
			}
		});
		adjustmentsTable.setDeleteKeyAction(() -> deletePayslipAdjustment());
		
		previewPayslipTable.getItems().setAll(payslip.getPreviewItems());
	}

	protected void editSelectedAttendance() {
		Map<String, Object> model = new HashMap<>();
		model.put("employeeAttendance", attendancesTable.getSelectionModel().getSelectedItem());
		
		employeeAttendanceDialog.showAndWait(model);
		
		updateDisplay();
	}

	protected void editSelectedAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslipAdjustment", adjustmentsTable.getSelectionModel().getSelectedItem());
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void doOnBack() {
		stageController.showPayrollScreen(payslip.getPayroll());
	}

	@FXML public void editPayslip() {
		stageController.showEditPayslipScreen(payslip);
	}

	@FXML public void addPayslipAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}
	
	@FXML public void editPayslipAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void deletePayslipAdjustment() {
		if (!isAdjustmentSelected()) {
			ShowDialog.error("No payment selected");
			return;
		}
		
		if (!ShowDialog.confirm("Delete payslip adjustment?")) {
			return;
		}
		
		try {
			payrollService.delete(adjustmentsTable.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip adjustment deleted");
		updateDisplay();
	}

	private boolean isAdjustmentSelected() {
		return adjustmentsTable.getSelectionModel().getSelectedItem() != null;
	}

	@FXML 
	public void deleteEmployeeAttendance() {
		if (!isEmployeeAttendanceSelected()) {
			return;
		}
		
		if (ShowDialog.confirm("Delete selected employee attendance?")) {
			try {
				employeeService.deleteEmployeeAttendance(getSelectedEmployeeAttendance());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Employee attendance deleted");
			updateDisplay();
		}
	}

	private boolean isEmployeeAttendanceSelected() {
		return getSelectedEmployeeAttendance() != null;
	}
	
	private EmployeeAttendance getSelectedEmployeeAttendance() {
		return attendancesTable.getSelectionModel().getSelectedItem();
	}

	@FXML public void addValeProduct() {
		if (!canConnectToMagic()) {
			ShowDialog.error("Cannot connet to MAGIC");
			return;
		};
		
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		addValeProductDialog.showAndWait(model);
		
		updateDisplay();
	}

	private boolean canConnectToMagic() {
		try {
			valeProductService.findUnpaidValeProductsByEmployee(payslip.getEmployee());
		} catch (ConnectToMagicException e) {
			return false;
		}
		
		return true;
	}

	@FXML public void deleteValeProduct() {
		if (hasValeProductSelected() && ShowDialog.confirm("Delete selected vale product?")) {
			try {
				payrollService.delete(getSelectedValeProduct());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Vale product deleted");
			updateDisplay();
		}
	}

	private boolean hasValeProductSelected() {
		return getSelectedValeProduct() != null;
	}

	private ValeProduct getSelectedValeProduct() {
		return valeProductsTable.getSelectionModel().getSelectedItem();
	}

	@FXML public void addLoanPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		addPayslipLoanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void deleteLoanPayment() {
		if (hasNoSelectedLoanPayment()) {
			ShowDialog.error("No loan payment selected");
			return;
		}
		
		if (ShowDialog.confirm("Delete selected loan payment?")) {
			try {
				employeeLoanService.delete(getSelectedLoanPayment());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Loan payment deleted");
			updateDisplay();
		}
	}

	private boolean hasNoSelectedLoanPayment() {
		return getSelectedLoanPayment() == null;
	}

	private EmployeeLoanPayment getSelectedLoanPayment() {
		return loanPaymentsTable.getSelectedItem();
	}

	@FXML 
	public void deletePayslip() {
		if (!ShowDialog.confirm("Delete payslip?")) {
			return;
		}
		
		try {
			payrollService.delete(payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip deleted");
		stageController.showPayrollScreen(payslip.getPayroll());
	}

}
