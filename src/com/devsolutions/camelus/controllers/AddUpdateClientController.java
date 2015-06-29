package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUpdateClientController implements Initializable {

	@FXML
	private TextField enterpriseNameField;
	
	@FXML
	private TextField addressField;
	
	@FXML
	private TextField contactNameField;
	
	@FXML
	private TextField contactPhoneField;
	
	@FXML
	private TextField contactEmailField;
	
	@FXML
	private Button btn;
	
	@FXML
	private Button endBtn;
	
	private Stage stage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		btn.setOnAction(e -> {
			stage = (Stage) btn.getScene().getWindow();
			stage.close();
		});
		
		endBtn.setOnAction(e -> {
			stage = (Stage) endBtn.getScene().getWindow();
			stage.close();
		});
	}

}
