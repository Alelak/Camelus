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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.devsolutions.camelus.application.*;

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
	private Button tbsettings;
	@FXML
	private GridPane header;
	@FXML
	private Label lblClose;
	@FXML
	private Label lblMinimize;
	@FXML
	private Label lblHelp;
	@FXML
	private HBox content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
		fadeTransition = new FadeTransition(Duration.millis(2000), content);
		switchScene("home");

		tbacceuilbtn.setOnAction(e -> {
			resetButtonColor();
			tbacceuilbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("Home");
		});

		tbclientsbtn.setOnAction(e -> {
			resetButtonColor();
			tbclientsbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showclients");
		});

		tbvendeursbtn.setOnAction(e -> {
			resetButtonColor();
			tbvendeursbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showvendors");
		});

		tbcommands.setOnAction(e -> {
			resetButtonColor();
			tbcommands.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showorders");
		});

		tbproduitsbtn.setOnAction(e -> {
			resetButtonColor();
			tbproduitsbtn.setStyle("-fx-background-color: #00A0DC;");
			switchScene("showproducts");
		});

		tbsettings
				.setOnAction(e -> {
					resetButtonColor();
					tbsettings.setStyle("-fx-background-color: #00A0DC;");

					try {

						CustomInfoBox cdb = new CustomInfoBox(
								stage,
								"Salut \nLe Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500?",
								"OK");
						cdb.btn.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
							Stage boxStage = (Stage) cdb.btn.getScene().getWindow();
							boxStage.close();
						
							}
						});

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
	}

	public void resetButtonColor() {
		tbproduitsbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbcommands.setStyle("-fx-background-color: -camelus-blue;");
		tbvendeursbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbclientsbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbacceuilbtn.setStyle("-fx-background-color: -camelus-blue;");
		tbsettings.setStyle("-fx-background-color: -camelus-blue;");
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

	public void switchScene(final String filename) {
		fadeTransition.stop();
		Duration dr = new Duration(500);
		fadeTransition.setDuration(dr);
		fadeTransition.setFromValue(1.);
		fadeTransition.setToValue(0.);
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
				fadeTransition = new FadeTransition(Duration.millis(500),
						content);
				fadeTransition.setFromValue(0.);
				fadeTransition.setToValue(1.);
				fadeTransition.play();
			}
		});
		fadeTransition.play();
	}
}
