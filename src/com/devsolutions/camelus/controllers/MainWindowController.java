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
	private double initialX;
	private double initialY;
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
	private Label lblMinimiaze;
	@FXML
	private Label lblHelp;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addDraggableNode(header);
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

	private void addDraggableNode(final Node node) {
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

	@FXML
	private void CloseWindow() {
		stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}
}
