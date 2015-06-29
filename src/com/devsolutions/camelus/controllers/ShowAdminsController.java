package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.managers.AdminManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShowAdminsController implements Initializable {

	@FXML
	private TableView<Admin> adminsTableView;
	private ObservableList<Admin> adminsOb = FXCollections
			.observableArrayList();
	@FXML
	private TableColumn<Admin, Integer> idColumn;
	@FXML
	private TableColumn<Admin, String> fnameColumn;
	@FXML
	private TableColumn<Admin, String> lnameColumn;
	@FXML
	private TableColumn<Admin, String> loginColumn;
	@FXML
	private TableColumn<Admin, Date> hireDateColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<Admin, Integer>(
				"id"));
		fnameColumn
				.setCellValueFactory(new PropertyValueFactory<Admin, String>(
						"fname"));
		lnameColumn
				.setCellValueFactory(new PropertyValueFactory<Admin, String>(
						"lname"));
		loginColumn
				.setCellValueFactory(new PropertyValueFactory<Admin, String>(
						"login"));
		hireDateColumn
				.setCellValueFactory(new PropertyValueFactory<Admin, Date>(
						"hire_date"));
		adminsOb.setAll(AdminManager.getAll());
		adminsTableView.setItems(adminsOb);
	}

}
