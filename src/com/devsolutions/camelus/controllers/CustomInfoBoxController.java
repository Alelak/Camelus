package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class CustomInfoBoxController implements Initializable {

	private double initialX;
	private double initialY;
	@FXML
	private Label lblClose;
	@FXML
	private HBox toolBar;
	@FXML
	public Button btn;
	@FXML
	public Label msg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addDraggableNode(toolBar);

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

	public void setMessage(String text) {
		msg.setText(text);
	}

	public void setBtnText(String text) {
		btn.setText(text);
	}

	public void setTextColor(String color) {
		msg.setTextFill(Paint.valueOf(color));
	}

}
