package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.CustomInfoBox;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddUpdateClientController implements Initializable {
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
		FXUtils.addDraggableNode(titleBar);

		contact_telTxt
				.setOnKeyReleased(e -> {
					if (StringUtils.validPhoneNumber(contact_telTxt.getText()
							.trim())) {

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
							if (StringUtils.validPhoneNumber(contact_telTxt
									.getText().trim())) {
								String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
								Pattern pattern = Pattern.compile(regex);
								Matcher matcher = pattern
										.matcher(contact_telTxt.getText());
								contact_telTxt.setText((matcher
										.replaceFirst("($1) $2-$3")));
								contact_telTxt.setStyle("-fx-border-width: 0;");
							}
						}
					}
				});

		contact_emailTxt
				.setOnKeyReleased(e -> {
					if (StringUtils.validEmail(contact_emailTxt.getText()
							.trim())) {
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
							if (StringUtils.validEmail(contact_emailTxt
									.getText().trim())) {
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
						feedbackmsg += "Il faut saisir Tous les champs requis.\n";
					}

					if (clientToUpdate == null) {
						if (ClientByEntrepriseAndClientName != null) {
							feedbackmsg += "La combinaison de nom de l'entreprise et nom du client existe d�j�. \n";
							valid = false;
						}
					}
					if (!StringUtils.validPhoneNumber((phoneNumber))) {
						feedbackmsg += "Vous devez saisir un num�ro de t�l�phone valide. \n";
						valid = false;
					}

					if (!StringUtils.validEmail(email)) {
						feedbackmsg += "Vous devez saisir une adresse email valide. \n";
						valid = false;
					}

					if (valid) {
						if (clientToUpdate != null) {
							client.setId(clientToUpdate.getId());
							ClientManager.update(client);
							showClientsController.updateTable(index, client);
							showClientsController.selectTheModifierRow(index);
						} else {
							ClientManager.add(client);
							showClientsController.addToTable(client);
							showClientsController.showTableView();
							showClientsController.selectLastRow();
						}
						stage.close();
					} else {
						try {
							CustomInfoBox customDialogBox = new CustomInfoBox(
									stage, feedbackmsg, "Ok", "#282828");
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

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
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