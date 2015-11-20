package com.pj.hrapp.dialog;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.exception.ConnectToMagicException;
import com.pj.hrapp.exception.NoMagicCustomerCodeException;
import com.pj.hrapp.exception.ValeProductsNotMarkedException;
import com.pj.hrapp.gui.component.SelectableTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.model.util.TableItem;
import com.pj.hrapp.service.EmployeeLoanService;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.service.ValeProductService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;

@Component
public class AddPayslipDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(AddPayslipDialog.class);
	
	@Autowired private EmployeeService employeeService;
	@Autowired private PayrollService payrollService;
	@Autowired private ValeProductService valeProductService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private TableView<Employee> employeesTable;
	@FXML private SelectableTableView<ValeProduct> valeProductsTable;
	@FXML private SelectableTableView<EmployeeLoan> employeeLoansTable;
	
	@Parameter private Payroll payroll;
	
	private Payslip payslip;
	
	@Override
	public void updateDisplay() {
		employeesTable.getItems().setAll(employeeService.findAllActiveEmployeesNotInPayroll(payroll));
	}
	
	private void updateValeProductsTable() {
		valeProductsTable.getItems().clear();
		
		try {
			for (ValeProduct valeProduct : valeProductService.findUnpaidValeProductsByEmployee(payslip.getEmployee())) {
				valeProductsTable.getItems().add(new TableItem<ValeProduct>(valeProduct));
			}
		} catch (NoMagicCustomerCodeException e) {
			ShowDialog.error("Employee MAGIC customer code not specified");
		} catch (ConnectToMagicException e) {
			ShowDialog.error("Cannot connect to MAGIC");
		}
	}

	@Override
	protected String getDialogTitle() {
		return "Add Payslip - Select Employee";
	}

	@Override
	protected String getSceneName() {
		return "addPayslipDialog";
	}

	@FXML
	public void createPayslip() {
		if (hasNoSelectedEmployee()) {
			ShowDialog.error("No employee selected");
			return;
		}
		
		if (!ShowDialog.confirm("Create payslip for employee?")) {
			hide();
			return;
		}
		
		payslip = new Payslip();
		payslip.setPayroll(payroll);
		payslip.setEmployee(employeesTable.getSelectionModel().getSelectedItem());
		payslip.setPeriodCovered(payroll.getPeriodCovered());
		
		try {
			payrollService.save(payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		switchToAddLoanPaymentsScreen();
	}
	
	private void switchToAddLoanPaymentsScreen() {
		changeScene("addPayslipDialog-loanPayment", 800d, 400d);
		setTitle("Add Payslip - Add Loan Payments");
		updateEmployeeLoansTable();
	}

	private void updateEmployeeLoansTable() {
		employeeLoansTable.setItems(employeeLoanService.findAllUnpaidLoansByEmployee(payslip.getEmployee()));
	}

	@FXML
	public void switchToAddValeProductScreen() {
		changeScene("addPayslipDialog-valeProduct");
		setTitle("Add Payslip - Add Vale Products");
		updateValeProductsTable();
	}

	private boolean hasNoSelectedEmployee() {
		return getSelectedEmployee() == null;
	}

	private Employee getSelectedEmployee() {
		return employeesTable.getSelectionModel().getSelectedItem();
	}

	private void changeScene(String sceneName) {
		changeScene(sceneName, sceneWidth, sceneHeight);
	}
	
	private void changeScene(String sceneName, double sceneWidth, double sceneHeight) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setController(this);
		
		Parent root = null;
		try {
			root = fxmlLoader.load(getClass().getResourceAsStream("/fxml/" + sceneName + ".fxml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Scene scene = new Scene(root, sceneWidth, sceneHeight);
		scene.getStylesheets().add("css/application.css");
		setScene(scene);
	
		updateDisplay();
	}
	
	@FXML
	public void addAllValeProducts() {
		if (employeeHasNoValeProducts()) {
			ShowDialog.error("No vale products to add");
		} else {
			addValeProductsToPayslip(valeProductsTable.getAllItems());
		}
	}
	
	private boolean employeeHasNoValeProducts() {
		return valeProductsTable.hasNoItems();
	}

	@FXML
	public void addSelectedValeProducts() {
		if (hasNoValeProductsSelected()) {
			ShowDialog.error("No vale products selected");
		} else {
			addValeProductsToPayslip(valeProductsTable.getSelectedItems());
		}
	}

	private boolean hasNoValeProductsSelected() {
		return !valeProductsTable.hasSelected();
	}

	private void addValeProductsToPayslip(List<ValeProduct> valeProducts) {
		if (!ShowDialog.confirm("Adding vale products will mark them as paid. Proceed?")) {
			return;
		}
		
		try {
			valeProductService.addValeProductsToPayslip(valeProducts, payslip);
		} catch (ValeProductsNotMarkedException e) {
			ShowDialog.error("Error marking vale products as paid in MAGIC system");
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Vale Products saved");
		hide();
	}
	
	@FXML
	public void addAllLoanPayments() {
		if (employeeHasNoUnpaidLoans()) {
			ShowDialog.error("No loan payments to add");
		} else {
			addLoanPaymentsToPayslip(employeeLoansTable.getAllItems());
		}
	}

	private boolean employeeHasNoUnpaidLoans() {
		return employeeLoansTable.hasNoItems();
	}

	@FXML
	public void addSelectedLoanPayments() {
		if (hasNoLoanPaymentsSelected()) {
			ShowDialog.error("No loan payments selected");
		} else {
			addLoanPaymentsToPayslip(employeeLoansTable.getSelectedItems());
		}
	}

	private boolean hasNoLoanPaymentsSelected() {
		return !employeeLoansTable.hasSelected();
	}
	
	private void addLoanPaymentsToPayslip(List<EmployeeLoan> loans) {
		try {
			employeeLoanService.createLoanPayments(loans, payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			hide();
			return;
		}
		
		ShowDialog.info("Loan payments saved");
		switchToAddValeProductScreen();
	}

}