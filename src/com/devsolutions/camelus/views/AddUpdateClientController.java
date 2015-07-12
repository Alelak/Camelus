package com.devsolutions.camelus.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.devsolutions.camelus.Listeners.AutoCompleteComboBoxListener;
import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
	private ComboBox<Vendor> vendorCombobox;

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

	private ObservableList<Vendor> vendorOservableList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		vendorOservableList = FXCollections.observableArrayList();
		vendorCombobox.setItems(vendorOservableList);
		if (Session.vendor == null) {

			vendorOservableList.addAll(VendorManager.getAll());
		} else {
			vendorOservableList.add(Session.vendor);
			vendorCombobox.getSelectionModel().select(index);
			vendorCombobox.setDisable(true);
		}

		new AutoCompleteComboBoxListener<Vendor>(vendorCombobox);

		contact_telTxt.setOnKeyReleased(e -> {
			if (StringUtils.validPhoneNumber(contact_telTxt.getText().trim())) {

				contact_telTxt.setStyle(FXUtils.HAS_SUCCESS);

			} else {
				contact_telTxt.setStyle(FXUtils.HAS_ERROR);
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

		contact_emailTxt.setOnKeyReleased(e -> {
			if (StringUtils.validEmail(contact_emailTxt.getText().trim())) {
				contact_emailTxt.setStyle(FXUtils.HAS_SUCCESS);
			} else {
				contact_emailTxt.setStyle(FXUtils.HAS_ERROR);
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
					int associated_vendor = 0;
					if (vendorCombobox.getSelectionModel().getSelectedIndex() > -1) {
						int index = vendorCombobox.getSelectionModel()
								.getSelectedIndex();
						associated_vendor = vendorOservableList.get(index)
								.getId();
					} else {
						valid = false;
						feedbackmsg += "Il faut choisir un vendeur.\n";
					}

					if (enterprise.isEmpty() || name.isEmpty()
							|| address.isEmpty() || email.isEmpty()) {
						valid = false;
						feedbackmsg += "Il faut saisir Tous les champs requis.\n";
					}

					if (clientToUpdate == null) {
						if (enterprise.isEmpty() && !name.isEmpty()) {
							if (ClientManager.getByEntrepriseAndClientName(
									enterprise, name) != null) {
								feedbackmsg += "La combinaison de nom de l'entreprise et nom du client existe déjà. \n";
								valid = false;
							}
						}
					}
					if (!StringUtils.validPhoneNumber((phoneNumber))) {
						feedbackmsg += "Vous devez saisir un numéro de téléphone valide. \n";
						valid = false;
					}

					if (!StringUtils.validEmail(email)) {
						feedbackmsg += "Vous devez saisir une adresse email valide. \n";
						valid = false;
					}

					if (valid) {
						Client client = new Client();
						client.setEnterprise_name(enterprise);
						client.setContact_name(name);
						client.setContact_tel(phoneNumber);
						client.setContact_email(email);
						client.setAddress(address);
						client.setDescription(description);
						client.setAssociated_vendor(associated_vendor);
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
							new CustomInfoBox(stage, feedbackmsg, "Ok");
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
		for (Vendor vendor : vendorOservableList) {
			if (client.getAssociated_vendor() == vendor.getId()) {
				vendorCombobox.getSelectionModel().select(vendor);
			}
		}
	}

}