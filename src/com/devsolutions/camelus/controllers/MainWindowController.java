package com.devsolutions.camelus.controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.devsolutions.camelus.application.CustomDialogBox;
import com.devsolutions.camelus.entities.AccountTypes;
import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.entities.Vendor;

public class MainWindowController implements Initializable {
	private Stage stage;
	private double initialX;
	private double initialY;
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
	@FXML 
	private HBox content;
	private Admin admin;
	private Vendor vendor;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
		tbacceuilbtn.setOnAction(e -> {
			
			resetButtonColor();
			tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
			
			FXMLLoader loader = new FXMLLoader( getClass().getResource( "../views/Home.fxml" ));
			try {
				content.getChildren().setAll(loader.load());
			} catch (IOException j) {
				// TODO Auto-generated catch block
				j.printStackTrace();
			}			
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

	public void setMainApp(Stage stage) {
		this.stage = stage;
	}

	public void addDraggableNode(final Node node) {
		node.setOnMousePressed(e -> {
			if (e.getButton() != MouseButton.MIDDLE) {
				initialX = e.getSceneX();
				initialY = e.getSceneY();
			}
		});
		node.setOnMouseDragged(e -> {
			if (e.getButton() != MouseButton.MIDDLE) {
				node.getScene().getWindow().setX(e.getScreenX() - initialX);
				node.getScene().getWindow().setY(e.getScreenY() - initialY);
			}
		});
	}

	public void initData(AccountTypes type, Object obj) {
		switch (type) {
		case ADMIN:
			admin = (Admin) obj;
			break;
		case SUPERADMIN:
			admin = (Admin) obj;
			break;
		case VENDOR:
			vendor = (Vendor) obj;
			break;
		}

	}
}
