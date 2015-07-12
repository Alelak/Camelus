package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.utils.FXUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CustomDialogBoxController implements Initializable {

	@FXML
	private Label lblClose;
	@FXML
	private HBox toolBar;
	@FXML
	public Button positiveButton;
	@FXML
	public Button negativeButton;
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

	public void setPositiveButtonText(String text) {
		positiveButton.setText(text);
	}

	public void setNegativeButtonText(String text) {
		negativeButton.setText(text);
	}
}
