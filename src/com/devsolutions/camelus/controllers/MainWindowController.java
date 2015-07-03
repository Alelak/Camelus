package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.CustomDialogBox;
import com.devsolutions.camelus.utils.FontAwesomeIconView;

public class MainWindowController implements Initializable {
	private Stage stage;
	private double initialX;
	private double initialY;
	private FadeTransition fadeTransition;
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
	private Label lblHelp;
	@FXML
	private HBox content;
	@FXML
	private FontAwesomeIconView logoutbtn;
	@FXML
	private HBox innerToolbarHbox;
	@FXML
	private HBox outerToolbarHbox;

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
		}
		tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
		switchScene("home", null);

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
								addDraggableNode(root);
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
			tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("Home", null);
		});

		tbclientsbtn.setOnAction(e -> {
			resetButtonColor();
			tbclientsbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showclients", null);
		});

		tbvendeursbtn.setOnAction(e -> {
			resetButtonColor();
			tbvendeursbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showvendors", null);
		});

		tbcommands.setOnAction(e -> {
			resetButtonColor();
			tbcommands.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showorders", null);
		});

		tbproduitsbtn.setOnAction(e -> {
			resetButtonColor();
			tbproduitsbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showproducts", null);
		});
		tbadminsbtn.setOnAction(e -> {
			resetButtonColor();
			tbadminsbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showadmins", null);
		});
	}

	public void resetButtonColor() {
		tbproduitsbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbcommands.setStyle("-fx-background-color: -camelus-blue;");
		tbvendeursbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbclientsbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbacceuilbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbadminsbtn.setStyle("-fx-background-color: -camelus-blue;");
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

	public void addDraggableNode(final Node node) {
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

	public void loadScene(final String filename, Vendor selectedVendor) {
		resetButtonColor();
		tbcommands.setStyle("-fx-background-color: #00A0DC;");
		switchScene(filename, selectedVendor);
	}

	public void switchScene(final String filename, Vendor selectedVendor) {
		MainWindowController c = this;
		fadeTransition.stop();
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/" + filename + ".fxml"));
				try {
					content.getChildren().setAll(loader.load());
				} catch (IOException j) {
					j.printStackTrace();
				}

				if (filename.equals("showvendors")) {
					ShowVendorsController controller = loader
							.<ShowVendorsController> getController();
					controller.initData(c);
				}
				if (selectedVendor != null)
					if (filename.equals("showorders")) {
						ShowOrdersController controller = loader
								.<ShowOrdersController> getController();
						controller.initData(selectedVendor);
					}
				fadeTransition = new FadeTransition(Duration.millis(200),
						content);
				fadeTransition.setFromValue(0);
				fadeTransition.setToValue(1);
				fadeTransition.play();
			}
		});
		fadeTransition.play();
	}
}
