package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.CustomInfoBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddUpdateClientController implements Initializable {
	private double initialX;
	private double initialY;
	private Client clientToUpdate;
	private int index;
	private Stage stage;
	
	private ShowClientsController showClientsController;
	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Button btnAddUpdate;
	@FXML
	private Button btnCancel;
	@FXML
	private Label titleWindow;
	@FXML
	private TextField enterprise_nameTxt;
	@FXML
	private TextField contact_nameTxt;
	@FXML
	private TextField contact_telTxt;
	@FXML
	private TextField contact_emailTxt;
	@FXML
	private TextField addressTxt;
	@FXML
	private TextArea descriptionTxt;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addDraggableNode(titleBar);

		contact_telTxt
				.setOnKeyReleased(e -> {
					if (validatePhoneNumber(contact_telTxt.getText().trim())) {
						contact_telTxt
								.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");

					} else {
						contact_telTxt
								.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
					}
				});

		contact_telTxt.focusedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(
							ObservableValue<? extends Boolean> arg0,
							Boolean oldPropertyValue, Boolean newPropertyValue) {
						if (!newPropertyValue) {
							if (contact_telTxt.getText().isEmpty())
								contact_telTxt.setStyle("-fx-border-width: 0;");
							if (validatePhoneNumber(contact_telTxt.getText())) {
								contact_telTxt.setStyle("-fx-border-width: 0;");
								contact_telTxt.setText(String.format(
										"(%s) %s-%s",
										contact_telTxt.getText()
												.substring(0, 3),
										contact_telTxt.getText()
												.substring(3, 6),
										contact_telTxt.getText().substring(6,
												10)));
							}
						}
					}
				});

		contact_emailTxt
				.setOnKeyReleased(e -> {
					if (validateEmail(contact_emailTxt.getText().trim())) {
						contact_emailTxt
								.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");
					} else {
						contact_emailTxt
								.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
					}
				});

		contact_emailTxt.focusedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(
							ObservableValue<? extends Boolean> arg0,
							Boolean oldPropertyValue, Boolean newPropertyValue) {
						if (!newPropertyValue) {
							if (contact_emailTxt.getText().isEmpty())
								contact_emailTxt
										.setStyle("-fx-border-width: 0;");
							if (validateEmail(contact_emailTxt.getText())) {
								contact_emailTxt
										.setStyle("-fx-border-width: 0;");
							}
						}
					}
				});

		btnAddUpdate.setOnAction(e -> {
			stage = (Stage) btnAddUpdate.getScene().getWindow();
			stage.close();
		});

		btnCancel.setOnAction(e -> {
			stage = (Stage) btnCancel.getScene().getWindow();
			stage.close();
		});

		btnAddUpdate
				.setOnAction(e -> {
					stage = (Stage) btnAddUpdate.getScene().getWindow();
					boolean valid = true;
					String feedbackmsg = "";
					String enterprise = enterprise_nameTxt.getText().trim();
					String name = contact_nameTxt.getText().trim();
					String email = contact_emailTxt.getText().trim();
					String phoneNumber = contact_telTxt.getText().trim();
					String address = addressTxt.getText().trim();
					String description = descriptionTxt.getText().trim();

					Client client = new Client();
					client.setEnterprise_name(enterprise);
					client.setContact_name(name);
					client.setContact_tel(phoneNumber);
					client.setContact_email(email);
					client.setAddress(address);
					client.setDescription(description);
					client.setAssociated_vendor(Session.vendor.getId());
					Client ClientByEntrepriseAndClientName = ClientManager
							.getByEntrepriseAndClientName(client);

					if (enterprise.isEmpty() || name.isEmpty()
							|| address.isEmpty() || email.isEmpty()) {
						valid = false;
						feedbackmsg += "Tous les champs avec une étoile sont requis.\n";
					}

					if (ClientByEntrepriseAndClientName != null) {
						feedbackmsg += "La combinaison de nom de l'entreprise et nom du client existe déjà. \n";
						valid = false;
					}

					if (!validatePhoneNumber(phoneNumber)) {
						feedbackmsg += "Le numéro de téléphone est incorrect. \n";
						valid = false;
					}

					if (!validateEmail(email)) {
						feedbackmsg += "L'adresse email est incorrecte. \n";
						valid = false;
					}

					if (valid) {

						if (clientToUpdate != null) {
							client.setId(clientToUpdate.getId());
							ClientManager.update(client);
							showClientsController.updateTable(index, client);
							showClientsController.showTableView();
						} else {
							ClientManager.add(client);
							showClientsController.addToTable(client);
							showClientsController.showTableView();
						}
						stage.close();
					} else {
						try {
							CustomInfoBox customDialogBox = new CustomInfoBox(
									stage, feedbackmsg, "Ok", "#ff0000");
							customDialogBox.btn
									.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											stage = (Stage) customDialogBox.btn
													.getScene().getWindow();
											stage.close();
										}
									});
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				});
	}

	public boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private boolean validatePhoneNumber(String phoneNo) {
		return (isNumeric(phoneNo) && phoneNo.length() == 10) ? true : false;
	}

	private boolean validateEmail(String email) {
		Pattern ptr = Pattern
				.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$");
		return (ptr.matcher(email).matches()) ? true : false;
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

	public void initStageAndData(ShowClientsController showClientsController,
			Client client, int index, CRUD type) {
		this.showClientsController = showClientsController;
		switch (type) {
		case CREATE:
			titleWindow.setText("Ajouter Client");
			break;
		case UPDATE:
			titleWindow.setText("Modifier Client");
			btnAddUpdate.setText("Modifier");
			this.clientToUpdate = client;
			this.index = index;
			setData(clientToUpdate);
			break;
		default:
			titleWindow.setText("Default");
			break;
		}
	}

	private void setData(Client client) {
		enterprise_nameTxt.setText(client.getEnterprise_name());
		contact_nameTxt.setText(client.getContact_name());
		contact_telTxt.setText(client.getContact_tel());
		contact_emailTxt.setText(client.getContact_email());
		addressTxt.setText(client.getAddress());
		descriptionTxt.setText(client.getDescription());
	}

}