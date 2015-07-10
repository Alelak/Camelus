package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.utils.FXUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
	private Label lblClose;

	@FXML
	private Button btnClose;
	@FXML
	private GridPane titleBar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		btnClose.setOnAction(e -> CloseWindow());
	}

	public void initData(Vendor currentVendor) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		usernameLabel.setText(currentVendor.getLogin());
		fnameLabel.setText(currentVendor.getFname());
		lnameLabel.setText(currentVendor.getLname());
		hiredateLabel.setText(simpleDateFormat.format(currentVendor
				.getHire_date()));
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

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}
}
