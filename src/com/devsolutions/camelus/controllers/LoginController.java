package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.AdminManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.services.Session;

public class LoginController implements Initializable {
	@FXML
	private Button loginbtn;
	@FXML
	private TextField usernametxt;
	@FXML
	private TextField passwordtxt;
	@FXML
	private ChoiceBox<String> chooseAccountType;
	@FXML
	private ProgressIndicator progressIndicator;
	private ObservableList<String> accountTypes;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		accountTypes = FXCollections.observableArrayList();
		accountTypes.addAll("Type de Compte", "Admin", "Vendeur");
		chooseAccountType.setItems(accountTypes);
		progressIndicator.visibleProperty().bind(loginbtn.armedProperty());
		// test
		chooseAccountType.getSelectionModel().select(1);
		usernametxt.setText("admin");
		passwordtxt.setText("1234");

		loginbtn.setOnAction(e -> {

			loginLogic();
		});
		passwordtxt.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					loginLogic();
				}

			}
		});
	}

	@FXML
	private void CloseWindow() {
		stage = (Stage) loginbtn.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void MinimizeWindow() {
		stage = (Stage) loginbtn.getScene().getWindow();
		stage.setIconified(true);
	}

	public void loginLogic() {
		Platform.runLater(() -> {
			stage = (Stage) loginbtn.getScene().getWindow();
			String username = usernametxt.getText().trim();
			String password = passwordtxt.getText().trim();
			int accountType = chooseAccountType.getSelectionModel()
					.getSelectedIndex();
			if (!username.isEmpty() && !password.isEmpty() && accountType != 0) {

				if (accountType == 1) {

					Admin admin = AdminManager.getByUserName(username);
					if (admin != null) {

						if (admin.getPassword().equals(password)) {
							Session.admin = admin;
							FXMLLoader loader = new FXMLLoader(getClass()
									.getResource("../views/mainwindow.fxml"));
							Parent root = null;
							try {
								root = loader.load();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							MainWindowController mainWindowController = loader
									.<MainWindowController> getController();
							mainWindowController.setMainApp(stage);
							mainWindowController.addDraggableNode(root);
							Scene scene = new Scene(root);
							scene.getStylesheets().add(
									getClass().getResource("../views/main.css")
											.toExternalForm());
							stage.setScene(scene);
							stage.centerOnScreen();
						} else {
							showError();

						}
					} else {
						showError();

					}

				} else {
					Vendor vendor = VendorManager.getByUserName(username);
					if (vendor != null) {
						if (vendor.getPassword().equals(password)) {
							Session.vendor = vendor;
							FXMLLoader loader = new FXMLLoader(getClass()
									.getResource("../views/mainwindow.fxml"));
							Parent root = null;
							try {
								root = loader.load();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							MainWindowController mainWindowController = loader
									.<MainWindowController> getController();
							mainWindowController.setMainApp(stage);
							mainWindowController.addDraggableNode(root);
							Scene scene = new Scene(root);

							scene.getStylesheets().add(
									getClass().getResource("../views/main.css")
											.toExternalForm());
							stage.setScene(scene);
							stage.centerOnScreen();
						} else {
							showError();
						}
					} else {
						showError();
					}
				}
			} else {
				if (username.isEmpty()) {
					usernametxt
							.setStyle("-fx-border-color:red; -fx-border-width:2");
				}
				if (password.isEmpty()) {
					passwordtxt
							.setStyle("-fx-border-color:red; -fx-border-width:2");
				}

				if (accountType == 0) {
					chooseAccountType
							.setStyle("-fx-border-color:red; -fx-border-width:2");
				}

			}

		});

	}

	public void showError() {
		usernametxt.setStyle("-fx-border-color:red; -fx-border-width:2");
		passwordtxt.setStyle("-fx-border-color:red; -fx-border-width:2");
	}
}
