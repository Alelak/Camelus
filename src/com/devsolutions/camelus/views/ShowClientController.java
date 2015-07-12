package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.utils.FXUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ShowClientController implements Initializable {
	@FXML
	private Label lblClose;
	@FXML
	private GridPane titleBar;
	@FXML
	private Label ContactName;
	@FXML
	private Label entrepriseName;
	@FXML
	private Label phoneNumber;
	@FXML
	private Label email;
	@FXML
	private Label adress;
	@FXML
	private TextArea description;
	@FXML
	private Button btnClose;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		btnClose.setOnAction(e -> {
			CloseWindow();
		});
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}

	public void initData(Client client) {
		ContactName.setText(client.getContact_name());
		entrepriseName.setText(client.getEnterprise_name());
		phoneNumber.setText(client.getContact_tel());
		email.setText(client.getContact_email());
		adress.setText(client.getAddress());
		description.setText(client.getDescription());
	}

}
