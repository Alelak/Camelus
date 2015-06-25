package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.CommissionManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ShowVendorDetailsController implements Initializable {
	@FXML
	private Label usernameLabel;

	@FXML
	private Label fnameLabel;

	@FXML
	private Label lnameLabel;

	@FXML
	private Label hiredateLabel;

	@FXML
	private Label sinLabel;

	@FXML
	private Label commissionLabel;

	@FXML
	private Label conditionLabel;

	@FXML
	private Label bonusLabel;

	@FXML
	private Label bonusTxtLabel;

	@FXML
	private Button endBtn;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		endBtn.setOnAction(e -> {
			stage = (Stage) endBtn.getScene().getWindow();
			stage.close();
		});
	}

	public void initData(Vendor currentVendor) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentVendor.getHire_date());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		usernameLabel.setText(currentVendor.getLogin());
		fnameLabel.setText(currentVendor.getFname());
		lnameLabel.setText(currentVendor.getLname());
		hiredateLabel.setText(year + "-" + month + "-" + day);
		sinLabel.setText(currentVendor.getSin());

		Commission commission = CommissionManager.getById(currentVendor
				.getCommission_id());

		if (commission.getType() == 0) {
			commissionLabel.setText("Pourcentage");
			bonusTxtLabel.setText("Bonus");
			bonusLabel.setText(commission.getRate() + "%");
		} else {
			commissionLabel.setText("Fixe");
			bonusLabel.setText(commission.getRate() + " $");
		}

		conditionLabel.setText(commission.getMcondition() + " $");

	}
}
