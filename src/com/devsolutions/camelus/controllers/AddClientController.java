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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AddClientController implements Initializable {
	private double initialX;
	private double initialY;

	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnCancel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addDraggableNode(titleBar);
		btnAdd.setOnAction(e-> {
			
			
			
			
			Stage stage = (Stage) btnAdd.getScene().getWindow();
			stage.close();
		});
		
		btnCancel.setOnAction(e-> {
			Stage stage = (Stage) btnCancel.getScene().getWindow();
			stage.close();
		});
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
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
}
