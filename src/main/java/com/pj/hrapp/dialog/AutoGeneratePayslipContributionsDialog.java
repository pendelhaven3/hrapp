package com.pj.hrapp.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Constants;
import com.pj.hrapp.Parameter;
import com.pj.hrapp.exception.NoMatchingSssContributionTableEntryException;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.PayrollService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Component
public class AutoGeneratePayslipContributionsDialog extends AbstractDialog {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoGeneratePayslipContributionsDialog.class);
	
	@Autowired private PayrollService payrollService;
	
	@FXML private TextField contributionMonthField;
	
	@Parameter private Payslip payslip;
    @Parameter private String contributionMonth;
	
	private boolean success;
	
	public AutoGeneratePayslipContributionsDialog() {
	    setSceneHeight(200d);
    }
	
    @Override
    protected String getDialogTitle() {
        return "Auto-Generate Contributions";
    }

    @Override
    protected void updateDisplay() {
        contributionMonthField.setText(contributionMonth);
    }

    @Override
    protected String getSceneName() {
        return "autoGenerateContributions";
    }
	
    @FXML
    public void generateContributions() {
        String month = contributionMonthField.getText();
        
        if (!isValidContributionMonth(month)) {
            ShowDialog.error("Contribution Month must be a valid month (MMYYYY)");
            contributionMonthField.requestFocus();
            return;
        }
        
        try {
            payrollService.regenerateGovernmentContributions(payslip, month);
        } catch (NoMatchingSssContributionTableEntryException e) {
            ShowDialog.error(e.getMessage());
            return;
        } catch (Exception e) {
            LOGGER.error("Unable to generate contributions", e);
            ShowDialog.unexpectedError();
            return;
        }
        
        ShowDialog.info("Government contributions generated");
        success = true;
        hide();
    }
    
    private boolean isValidContributionMonth(String month) {
        return month.matches(Constants.MONTH_YEAR_REGEX);
    }

    @FXML
    public void cancel() {
        hide();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
}