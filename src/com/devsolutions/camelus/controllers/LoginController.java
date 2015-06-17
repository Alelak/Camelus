package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	Button loginbtn;

	public LoginController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) loginbtn.getScene().getWindow();
		stage.close();
	}

}
