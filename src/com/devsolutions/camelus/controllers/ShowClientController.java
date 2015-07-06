package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
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
	private Double initialX;
	private Double initialY;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addDraggableNode(titleBar);		
		btnClose.setOnAction(e->{
			CloseWindow();
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

	public void initData(Client client) {
		ContactName.setText(client.getContact_name());
		entrepriseName.setText(client.getEnterprise_name());
		phoneNumber.setText(client.getContact_tel());
		email.setText(client.getContact_email());
		adress.setText(client.getAddress());
		description.setText(client.getDescription());
	}
	

}
