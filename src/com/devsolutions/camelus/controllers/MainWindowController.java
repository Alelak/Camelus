package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainWindowController implements Initializable {
	private Stage stage;

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
	@FXML
	private GridPane header;
	@FXML
	private Label lblClose;
	@FXML
	private Label lblMinimize;
	@FXML
	private Label lblHelp;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
		tbacceuilbtn.setOnAction(e -> {
			resetButtonColor();
			tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
		});
		tbclientsbtn.setOnAction(e -> {
			resetButtonColor();
			tbclientsbtn.setStyle("-fx-background-color: #00A0DC;");
		});
		tbvendeursbtn.setOnAction(e -> {
			resetButtonColor();
			tbvendeursbtn.setStyle("-fx-background-color: #00A0DC;");
		});

		tbcommands.setOnAction(e -> {
			resetButtonColor();
			tbcommands.setStyle("-fx-background-color: #00A0DC;");
		});
		tbproduitsbtn.setOnAction(e -> {
			resetButtonColor();
			tbproduitsbtn.setStyle("-fx-background-color: #00A0DC;");
		});
		tbsettings.setOnAction(e -> {
			resetButtonColor();
			tbsettings.setStyle("-fx-background-color: #00A0DC;");
		});
	}

	public void resetButtonColor() {
		tbproduitsbtn.setStyle("-fx-background-color: #0077B5;");
		tbcommands.setStyle("-fx-background-color: #0077B5;");
		tbvendeursbtn.setStyle("-fx-background-color: #0077B5;");
		tbclientsbtn.setStyle("-fx-background-color: #0077B5;");
		tbacceuilbtn.setStyle("-fx-background-color: #0077B5;");
		tbsettings.setStyle("-fx-background-color: #0077B5;");
	}

	@FXML
	private void CloseWindow() {
		stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}
}
