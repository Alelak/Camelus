package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.devsolutions.camelus.application.CustomDialogBox;
import com.devsolutions.camelus.application.CustomInfoBox;
import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.Choice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUpdateVendorController implements Initializable {
	@FXML
	private TextField textUsername;
	@FXML
	private TextField textPassword;
	@FXML
	private TextField textFname;
	@FXML
	private TextField textLname;
	@FXML
	private DatePicker hireDate;
	@FXML
	private TextField textSin;
	@FXML
	private ChoiceBox<Choice> commission;
	@FXML
	private Button btn;
	@FXML
	private Button btnCancel;

	private List<Commission> commissions;
	private ObservableList<Choice> commissionChoices;

	private ShowVendorsController vendorTVConroller;

	private Stage stage;

	private String btnName;
	private int index;

	private Vendor vendorToUpdate;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		commissions = CommissionManager.getAll();

		commissionChoices = FXCollections.observableArrayList();
		commissionChoices.add(new Choice(0, "No selection"));

		for (Commission commission : commissions) {
			if (commission.getType() == 0)
				commissionChoices.add(new Choice(commission.getId(), commission
						.getRate() + "%"));
			else
				commissionChoices.add(new Choice(commission.getId(), "Fixe : "
						+ commission.getRate() + "(>="
						+ commission.getMcondition() + ")"));
		}

		commission.setItems(commissionChoices);
		commission.getSelectionModel().select(0);

		btn.setOnAction(e -> {
			String invalidFields = "";
			boolean validfields = true;
			boolean passWordValid = textPassword
					.getText()
					.matches(
							"[0-9a-zA-Z][0-9a-zA-Z][0-9a-zA-Z][0-9a-zA-Z][0-9a-zA-Z][0-9a-zA-Z][0-9a-zA-Z][0-9a-zA-Z][A-Z][a-z]");

			for (Vendor vendor : vendorTVConroller.getTable().getItems()) {
				if (vendorToUpdate != null) {
					if (vendor.getLogin().equals(textUsername.getText()) && !vendor.getLogin().equals(vendorToUpdate.getLogin())) {
						validfields = false;
						invalidFields += " - Ce nom d'utilisateur est déjà utilisé par une autre personne \n";
					}
				}
				else
				{
					if (vendor.getLogin().equals(textUsername.getText())) {
						validfields = false;
						invalidFields += " - Ce nom d'utilisateur est déjà utilisé par une autre personne \n";
					}
				}
			}

			if (!passWordValid) {
				invalidFields += " - Votre mot de passe est invalide : Le mot de passe doit être constituer de 10 charactère. \n";
				invalidFields += " - Les 8 premier caractères du mot de passe doivent être soit des lettre ou des chiffres suivis d'une majuscule et en fin une miniscule. \n";
			}

			if (!textSin.getText().matches(
					"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
				invalidFields += " - Le NAS doit être un nombre de 9 chiffres \n";
				validfields = false;
			}

			if (textFname.getText().isEmpty() || textLname.getText().isEmpty()
					|| textUsername.getText().isEmpty()
					|| textSin.getText().isEmpty()
					|| textPassword.getText().isEmpty()) {
				invalidFields += " - Tous les champs doivent être remplis \n";
				validfields = false;
			}

			if (hireDate.getValue() == null) {
				invalidFields += " - Vous devez choisir une date d'embauche \n";
				validfields = false;
			}

			if (commission.getSelectionModel().getSelectedIndex() == 0) {
				invalidFields += " - Vous devez choisir une commission \n";
				validfields = false;
			}

			stage = (Stage) btnCancel.getScene().getWindow();

			if (validfields) {
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

				Date date = new Date();
				try {
					date = ft.parse(hireDate.getValue().toString());
				} catch (ParseException excep) {
					excep.printStackTrace();
				}

				Vendor vendor = new Vendor();

				vendor.setLogin(textUsername.getText());
				vendor.setPassword(textPassword.getText());
				vendor.setFname(textFname.getText());
				vendor.setLname(textLname.getText());
				vendor.setSin(textSin.getText());
				vendor.setCommission_id(commission.getSelectionModel()
						.getSelectedIndex());
				vendor.setHire_date(date);

				if (vendorToUpdate == null) {
					VendorManager.add(vendor);
					vendorTVConroller.addToTableView(vendor);
				} else {
					vendor.setId(vendorToUpdate.getId());
					VendorManager.update(vendor);
					vendorTVConroller.updateTableView(index, vendor);
				}

				stage.close();

			} else {
				try {
					CustomInfoBox customDialogBox = new CustomInfoBox(stage,
							invalidFields, "Ok");
					customDialogBox.btn
							.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									stage = (Stage) customDialogBox.btn
											.getScene().getWindow();
									stage.close();
								}
							});
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}

		});

		btnCancel.setOnAction(e -> {
			stage = (Stage) btnCancel.getScene().getWindow();
			stage.close();
		});
	}

	public void initData(ShowVendorsController vendorTVConroller,
			String btnName, Vendor vendorToUpdate, int index) {
		this.vendorTVConroller = vendorTVConroller;
		this.btnName = btnName;
		btn.setText(btnName);
		this.index = index;
		this.vendorToUpdate = vendorToUpdate;
		if (vendorToUpdate != null) {
			initForm();
		}
	}

	private void initForm() {
		textUsername.setText(vendorToUpdate.getLogin());
		textPassword.setText(vendorToUpdate.getPassword());
		textFname.setText(vendorToUpdate.getFname());
		textLname.setText(vendorToUpdate.getLname());

		Calendar cal = Calendar.getInstance();
		cal.setTime(vendorToUpdate.getHire_date());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		hireDate.setValue(LocalDate.of(year, month + 1, day));

		textSin.setText(String.valueOf(vendorToUpdate.getSin()));
		commission.getSelectionModel()
				.select(vendorToUpdate.getCommission_id());
	}

}
