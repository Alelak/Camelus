package com.devsolutions.camelus.views;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import com.devsolutions.camelus.utils.BoxType;
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
	@FXML
	private Label lblFacebook;
	@FXML
	private Label lblTwitter;
	@FXML
	private Label lblLinkedin;
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
			settingsmenubutton.getItems().removeAll(categoriesMI, UnitesMI,
					CommissionsMI, LogsMI);
		} else if (Session.admin.getSuper_admin() == 0) {
			GridPane.setMargin(outerToolbarHbox, new Insets(0, 60, 0, 0));
			innerToolbarHbox.getChildren().remove(tbadminsbtn);
			settingsmenubutton.getItems().remove(ProfileMi);
		} else {
			settingsmenubutton.getItems().remove(ProfileMi);
		}

		switchScene("Home");
		tbacceuilbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		logoutbtn.setOnMouseClicked(e -> {
			try {
				CustomDialogBox customDialogBox = new CustomDialogBox(stage,
						BoxType.QUESTION,
						"Voulez vous vraiment se deconnecter?", "Oui", "Non");
				customDialogBox.positiveButton
						.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								FXMLLoader loader = new FXMLLoader(getClass()
										.getResource("Login.fxml"));
								Parent root = null;
								try {
									root = loader.load();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								Scene scene = new Scene(root);
								scene.getStylesheets().add(
										getClass().getResource("main.css")
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
			switchScene("Home");
			resetButtonColor();
			tbacceuilbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		});

		tbclientsbtn.setOnAction(e -> {
			switchScene("ShowClients");
			resetButtonColor();
			tbclientsbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		});

		tbvendeursbtn.setOnAction(e -> {
			switchScene("ShowVendors");
			resetButtonColor();
			tbvendeursbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		});

		tbcommands.setOnAction(e -> {
			switchScene("ShowOrders");
			resetButtonColor();
			tbcommands.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		});

		tbproduitsbtn.setOnAction(e -> {
			switchScene("ShowProducts");
			resetButtonColor();
			tbproduitsbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		});
		tbadminsbtn.setOnAction(e -> {
			switchScene("ShowAdmins");
			resetButtonColor();
			tbadminsbtn.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
		});
		
		lblFacebook.setOnMouseClicked(e -> runLink("https://www.facebook.com/DevSolutions2015"));
		lblTwitter.setOnMouseClicked(e -> runLink("https://twitter.com/DevSolutions15"));
		lblLinkedin.setOnMouseClicked(e -> runLink("https://www.linkedin.com/"));
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

		try {
			stage = (Stage) lblClose.getScene().getWindow();

			CustomDialogBox customDialogBox = new CustomDialogBox(stage,
					BoxType.QUESTION,
					"Voulez vous vraiment quitter l'application?", "Oui", "Non");

			customDialogBox.positiveButton
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							customDialogBox.stage.close();
							stage.close();

						}
					});

		} catch (IOException e) {

			e.printStackTrace();
		}
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
													filename + ".fxml"));
									Parent root = null;
									try {
										root = loader.load();
										root.getStylesheets().add(
												getClass().getResource(
														"Content.css")
														.toExternalForm());
										content.getChildren().setAll(root);
									} catch (IOException j) {
										j.printStackTrace();
									}

									if (filename.equals("ShowVendors")) {
										ShowVendorsController controller = loader
												.<ShowVendorsController> getController();
										controller.initData(c);
									}

									if (filename.equals("ShowClients")) {
										ShowClientsController controller = loader
												.<ShowClientsController> getController();
										controller.initData(c);
									}

									if (vendor != null)
										if (filename.equals("ShowOrders")) {
											ShowOrdersController controller = loader
													.<ShowOrdersController> getController();
											controller.filterByVendor(vendor);
										}

									if (client != null)
										if (filename.equals("ShowOrders")) {
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
		switchScene(filename, vendor, null);
		tbvendeursbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbcommands.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
	}

	public void switchScene(final String filename, final Client client) {
		switchScene(filename, null, client);
		tbclientsbtn.setStyle(BACKGROUND_CAMELUS_BLUE);
		tbcommands.setStyle(BACKGROUND_CAMELUS_LIGHT_BLUE);
	}

	@FXML
	public void categoriesmihandler() {
		openMenuWindows("Categories");
	}

	@FXML
	public void commissionsmihandler() {
		openMenuWindows("Commissions");
	}

	@FXML
	public void unitsmihandler() {
		openMenuWindows("Units");
	}

	@FXML
	public void logsmihandler() {
		openMenuWindows("Logs");
	}

	@FXML
	public void profilemihandler() {
		openMenuWindows("VendorProfile");
	}

	@FXML
	public void showAboutWindow() {
		openMenuWindows("About");
	}

	private void openMenuWindows(String filename) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					filename + ".fxml"));

			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			scene.getStylesheets().addAll(
					getClass().getResource("childWindow.css").toExternalForm(),
					getClass().getResource("Content.css").toExternalForm());
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
	
	public void runLink(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e) {

			}
		}
	}
}
