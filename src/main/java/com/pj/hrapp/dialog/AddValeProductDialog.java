package com.pj.hrapp.dialog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.SelectableTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.model.util.TableItem;
import com.pj.hrapp.service.ValeProductService;

import javafx.fxml.FXML;

public class AddValeProductDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(AddValeProductDialog.class);
	
	@Autowired private ValeProductService valeProductService;
	
	@FXML private SelectableTableView<ValeProduct> valeProductsTable;
	
	@Parameter private Payslip payslip;
	
	@Override
	public void updateDisplay() {
		setTitle(getDialogTitle());
		
		valeProductsTable.getItems().clear();
		for (ValeProduct valeProduct : valeProductService.findUnpaidValeProductsByEmployee(payslip.getEmployee())) {
			valeProductsTable.getItems().add(new TableItem<ValeProduct>(valeProduct));
		}
	}
	
	@Override
	protected String getDialogTitle() {
		return "Add Vale Products to Payslip";
	}

	@Override
	protected String getSceneName() {
		return "addValeProductDialog";
	}

	@FXML
	public void addAll() {
		addValeProductsToPayslip(valeProductsTable.getAllItems());
	}
	
	@FXML
	public void addSelected() {
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
		try {
			valeProductService.addValeProductsToPayslip(valeProducts, payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Vale Products saved");
		hide();
	}
	
}