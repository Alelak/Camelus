package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MainWindowController implements Initializable {

	@FXML
	private Button tbacceuilbtn;
	@FXML
	private Button tbclientsbtn;
	@FXML
	private Button tbvendeursbtn;
	@FXML
	private Button tbproduitsbtn;
	@FXML
	private Button tbcommands;
	@FXML
	private Button tbsettings;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbacceuilbtn.setStyle("-fx-background-color: #03A9F4;");
		tbacceuilbtn.setOnAction(e -> {
			resetButtonColor();
			tbacceuilbtn.setStyle("-fx-background-color: #03A9F4;");
		});
		tbclientsbtn.setOnAction(e -> {
			resetButtonColor();
			tbclientsbtn.setStyle("-fx-background-color: #03A9F4;");
		});
		tbvendeursbtn.setOnAction(e -> {
			resetButtonColor();
			tbvendeursbtn.setStyle("-fx-background-color: #03A9F4;");
		});

		tbcommands.setOnAction(e -> {
			resetButtonColor();
			tbcommands.setStyle("-fx-background-color: #03A9F4;");
		});
		tbproduitsbtn.setOnAction(e -> {
			resetButtonColor();
			tbproduitsbtn.setStyle("-fx-background-color: #03A9F4;");
		});
		tbsettings.setOnAction(e -> {
			resetButtonColor();
			tbsettings.setStyle("-fx-background-color: #03A9F4;");
		});
	}

	public void resetButtonColor() {
		tbproduitsbtn.setStyle("-fx-background-color: #2196F3;");
		tbcommands.setStyle("-fx-background-color: #2196F3;");
		tbvendeursbtn.setStyle("-fx-background-color: #2196F3;");
		tbclientsbtn.setStyle("-fx-background-color: #2196F3;");
		tbacceuilbtn.setStyle("-fx-background-color: #2196F3;");
		tbsettings.setStyle("-fx-background-color: #2196F3;");
	}
}
