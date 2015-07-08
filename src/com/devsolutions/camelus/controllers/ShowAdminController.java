package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.utils.FXUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ShowAdminController implements Initializable {

	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Button btnClose;
	@FXML
	private Label logintxt;
	@FXML
	private Label sintxt;
	@FXML
	private Label hiredatetxt;
	@FXML
	private Label adminName;

	@FXML
	private Label idtxt;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);

		lblClose.setOnMouseClicked(e -> {
			stage = (Stage) lblClose.getScene().getWindow();
			stage.close();
		});
		btnClose.setOnAction(e -> {
			stage = (Stage) btnClose.getScene().getWindow();
			stage.close();
		});
	}

	public void initData(Admin admin) {
		idtxt.setText("" + admin.getId());
		adminName.setText(admin.getFull_name());
		logintxt.setText(admin.getLogin());
		sintxt.setText(admin.getSin());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		hiredatetxt.setText(simpleDateFormat.format(admin.getHire_date()));
	}
}
