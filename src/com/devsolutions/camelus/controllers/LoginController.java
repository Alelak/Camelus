package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.AdminManager;

import com.devsolutions.camelus.managers.VendorManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	private Button loginbtn;
	@FXML
	private TextField usernametxt;
	@FXML
	private TextField passwordtxt;
	@FXML
	private ChoiceBox<String> chooseAccountType;
	private ObservableList<String> accountTypes;

	public LoginController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		accountTypes = FXCollections.observableArrayList();
		accountTypes.addAll("Type de Compte", "Admin", "Vendeur");
		chooseAccountType.setItems(accountTypes);

		chooseAccountType.getSelectionModel().selectFirst();
		loginbtn.setOnAction(e -> {
			String username = usernametxt.getText().trim();
			String password = passwordtxt.getText().trim();
			int accountType = chooseAccountType.getSelectionModel()
					.getSelectedIndex();
			if (!username.isEmpty() && !password.isEmpty() && accountType != 0) {
				if (accountType == 1) {
					Admin admin = AdminManager.getByUserName(username);
					if (admin != null) {
						if (admin.getPassword().equals(password)) {
							if (admin.getSuper_admin() == 1) {
								// super admin
							} else {
								// normal admin
							}
						} else {
							usernametxt.getStyleClass().add("text-input-error");
							passwordtxt.getStyleClass().add("text-input-error");

						}
					} else {
						usernametxt.getStyleClass().add("text-input-error");
						passwordtxt.getStyleClass().add("text-input-error");

					}

				} else {
					Vendor vendor = VendorManager.getByUserName(username);
					if (vendor != null) {
						if (vendor.getPassword().equals(password)) {

						} else {
							usernametxt.getStyleClass().add("text-input-error");
							passwordtxt.getStyleClass().add("text-input-error");
						}
					} else {
						usernametxt.getStyleClass().add("text-input-error");
						passwordtxt.getStyleClass().add("text-input-error");
					}
				}
			} else {
				if (username.isEmpty()) {
					usernametxt.getStyleClass().add("text-input-error");
				}
				if (password.isEmpty()) {
					passwordtxt.getStyleClass().add("text-input-error");
				}
				if (accountType == 0) {
					chooseAccountType.getStyleClass().add("text-input-error");
				}

			}

		});
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) loginbtn.getScene().getWindow();
		stage.close();
	}

}
