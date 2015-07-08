package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.utils.FXUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class CustomInfoBoxController implements Initializable {

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
		FXUtils.addDraggableNode(toolBar);

	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
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
