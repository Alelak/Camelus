package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.CustomDialogBox;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.FontAwesomeIconView;

public class MainWindowController implements Initializable {
	@FXML
	private Button tbacceuilbtn;
	@FXML
	private Button tbclientsbtn;
	@FXML
	private Button tbvendeursbtn;
	@FXML
	private Button tbproduitsbtn;
	@FXML
	private Button tbcommands;
	@FXML
	private Button tbadminsbtn;
	@FXML
	private Label lblClose;
	@FXML
	private Label lblMinimize;
	@FXML
	private Label lblAbout;
	@FXML
	private HBox content;
	@FXML
	private FontAwesomeIconView logoutbtn;
	@FXML
	private HBox innerToolbarHbox;
	@FXML
	private HBox outerToolbarHbox;
	@FXML
	private MenuButton settingsmenubutton;
	@FXML
	private MenuItem categoriesMI;
	@FXML
	private MenuItem CommissionsMI;
	@FXML
	private MenuItem UnitesMI;
	@FXML
	private MenuItem LogsMI;
	@FXML
	private MenuItem ProfileMi;
	private Stage stage;
	private FadeTransition fadeTransition;

	private static final String BACKGROUND_CAMELUS_BLUE = "-fx-background-color: -camelus-blue;";
	private static final String BACKGROUND_CAMELUS_LIGHT_BLUE = "-fx-background-color: -camelus-light-blue; ";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fadeTransition = new FadeTransition();
		if (Session.vendor != null) {
			GridPane.setMargin(outerToolbarHbox, new Insets(0, 130, 0, 0));
			innerToolbarHbox.getChildren()
					.removeAll(tbvendeursbtn, tbadminsbtn);
		} else if (Session.admin.getSuper_admin() == 0) {
			GridPane.setMargin(outerToolbarHbox, new Insets(0, 60, 0, 0));
			innerToolbarHbox.getChildren().remove(tbadminsbtn);
		} else {
			// super admin
		}
		tbacceuilbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		switchScene("home");

		logoutbtn.setOnMouseClicked(e -> {
			try {
				CustomDialogBox customDialogBox = new CustomDialogBox(stage,
						"Voulez vous vraiment se deconnecter?", "Oui", "Non");
				customDialogBox.positiveButton
						.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								FXMLLoader loader = new FXMLLoader(getClass()
										.getResource("../views/login.fxml"));
								Parent root = null;
								try {
									root = loader.load();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								Scene scene = new Scene(root);
								scene.getStylesheets().add(
										getClass().getResource(
												"../views/main.css")
												.toExternalForm());
								stage.setScene(scene);
								stage.centerOnScreen();
								customDialogBox.stage.close();
								if (Session.admin != null) {
									Session.admin = null;
								} else {
									Session.vendor = null;
								}
							}
						});
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		tbacceuilbtn.setOnAction(e -> {
			resetButtonColor();
			tbacceuilbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
			switchScene("home");
		});

		tbclientsbtn.setOnAction(e -> {
			resetButtonColor();
			tbclientsbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
			switchScene("showclients");
		});

		tbvendeursbtn.setOnAction(e -> {
			resetButtonColor();
			tbvendeursbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
			switchScene("showvendors");
		});

		tbcommands.setOnAction(e -> {
			resetButtonColor();
			tbcommands.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
			switchScene("showorders");
		});

		tbproduitsbtn.setOnAction(e -> {
			resetButtonColor();
			tbproduitsbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
			switchScene("showproducts");
		});
		tbadminsbtn.setOnAction(e -> {
			resetButtonColor();
			tbadminsbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
			switchScene("showadmins");
		});
	}

	public void resetButtonColor() {
		tbproduitsbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbcommands.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbvendeursbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbclientsbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbacceuilbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbadminsbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
	}

	@FXML
	private void CloseWindow() {
		stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void MinimizeWindow() {
		stage = (Stage) lblMinimize.getScene().getWindow();
		stage.setIconified(true);
	}

	public void setMainApp(Stage stage) {
		this.stage = stage;
	}

	private void switchScene(final String filename, final Vendor vendor,
			final Client client) {
		MainWindowController c = this;
		new Thread(new Runnable() {

			@Override
			public void run() {
				Platform.runLater(() -> {
					fadeTransition.stop();
					fadeTransition.setFromValue(1);
					fadeTransition.setToValue(0);
					fadeTransition
							.setOnFinished(new EventHandler<ActionEvent>() {

								@Override
								public void handle(ActionEvent event) {
									FXMLLoader loader = new FXMLLoader(
											getClass().getResource(
													"../views/" + filename
															+ ".fxml"));
									try {
										content.getChildren().setAll(
												loader.load());
									} catch (IOException j) {
										j.printStackTrace();
									}

									if (filename.equals("showvendors")) {
										ShowVendorsController controller = loader
												.<ShowVendorsController> getController();
										controller.initData(c);
									}

									if (filename.equals("showclients")) {
										ShowClientsController controller = loader
												.<ShowClientsController> getController();
										controller.initData(c);
									}

									if (vendor != null)
										if (filename.equals("showorders")) {
											ShowOrdersController controller = loader
													.<ShowOrdersController> getController();
											controller.filterByVendor(vendor);
										}

									if (client != null)
										if (filename.equals("showorders")) {
											ShowOrdersController controller = loader
													.<ShowOrdersController> getController();
											controller.filterByClient(client);
										}
									fadeTransition = new FadeTransition(
											Duration.millis(200), content);
									fadeTransition.setFromValue(0);
									fadeTransition.setToValue(1);
									fadeTransition.play();
								}
							});
					fadeTransition.play();
				});
			}
		}).start();
	}

	private void switchScene(final String filename) {
		switchScene(filename, null, null);
	}

	public void switchScene(final String filename, final Vendor vendor) {
		tbvendeursbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbcommands.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		switchScene(filename, vendor, null);
	}

	public void switchScene(final String filename, final Client client) {
		tbclientsbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbcommands.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		switchScene(filename, null, client);
	}

	@FXML
	public void categoriesmihandler() {
		openMenuWindows("categories");
	}

	@FXML
	public void commissionsmihandler() {
		openMenuWindows("commissions");
	}

	@FXML
	public void unitsmihandler() {
		openMenuWindows("units");
	}

	@FXML
	public void logsmihandler() {
		openMenuWindows("logs");
	}

	@FXML
	public void profilemihandler() {
		if (Session.admin != null) {
			// admin

		} else {
			openMenuWindows("vendorprofile");
		}

	}

	@FXML
	public void showAboutWindow() {
		openMenuWindows("about");
	}

	private void openMenuWindows(String filename) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"../views/" + filename + ".fxml"));

			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(settingsmenubutton.getScene().getWindow());
			stage.show();
			FXUtils.centerStage((Stage) settingsmenubutton.getScene()
					.getWindow(), stage, 22);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
