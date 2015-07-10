package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ProfileController implements Initializable {
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
		// TODO Auto-generated method stub

	}

	@FXML
	private void CloseWindow() {
		((Stage) lblClose.getScene().getWindow()).close();
	}
}
