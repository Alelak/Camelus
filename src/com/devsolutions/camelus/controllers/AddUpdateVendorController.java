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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.AdminManager;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.utils.Choice;
import com.devsolutions.camelus.utils.CustomInfoBox;

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
	private int index;

	private Vendor vendorToUpdate;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		commissions = CommissionManager.getAll();

		commissionChoices = FXCollections.observableArrayList();
		commissionChoices.add(new Choice(0, "No selection"));

		for (Commission commission : commissions) {
			if (commission.getType() == 0)
				commissionChoices.add(new Choice(commission.getId(),
						"Poucentage : " + commission.getRate() + "%"));
			else
				commissionChoices.add(new Choice(commission.getId(), "Fixe : "
						+ commission.getRate() + "$ (>="
						+ commission.getMcondition() + "$)"));
		}

		commission.setItems(commissionChoices);
		commission.getSelectionModel().select(0);

		btn.setOnAction(e -> {
			String username = textUsername.getText().trim();
			String password = textPassword.getText().trim();
			String sin = textSin.getText().trim();
			String fname = textFname.getText().trim();
			String lname = textLname.getText().trim();

			String invalidFields = "";
			boolean validfields = true;
			Vendor vendorByLogin = VendorManager.getByUserName(username);
			Vendor vendorBySin = VendorManager.getBySin(sin);
			Admin adminBySin = AdminManager.getBySin(sin);
			if (fname.isEmpty() || lname.isEmpty() || username.isEmpty()
					|| sin.isEmpty() || password.isEmpty()) {
				invalidFields += " - Tous les champs doivent �tre remplis \n";
				validfields = false;
			}

			if (password.length() < 8) {
				invalidFields += " - Mot de passe de 8 caract�res et plus \n";
				validfields = false;
			}

			if (!sin.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
				invalidFields += " - Le NAS doit �tre un nombre de 9 chiffres \n";
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

			if (vendorToUpdate != null) {
				if (vendorByLogin != null
						&& !vendorByLogin.getLogin().equals(
								vendorToUpdate.getLogin())) {
					validfields = false;
					invalidFields += " - Ce nom d'utilisateur a �tait d�j� choisie \n";
				}

				if (adminBySin != null
						&& vendorBySin != null
						&& !vendorBySin.getSin()
								.equals(vendorToUpdate.getSin())
						&& !adminBySin.getSin().equals(vendorToUpdate.getSin())) {
					validfields = false;
					invalidFields += " - Ce NAS existe d�j�, veuillez saisir un NAS valide. \n";
				}
			} else {
				if (vendorByLogin != null) {
					validfields = false;
					invalidFields += " - Ce nom d'utilisateur a �tait d�j� choisie \n";
				}

				if (vendorByLogin != null && adminBySin != null) {
					validfields = false;
					invalidFields += " - Ce NAS existe d�j�, veuillez saisir un NAS valide. \n";
				}
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

				vendor.setLogin(username);
				vendor.setPassword(password);
				vendor.setFname(fname);
				vendor.setLname(lname);
				vendor.setSin(sin);
				vendor.setCommission_id(commission.getSelectionModel()
						.getSelectedItem().getId());
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
							invalidFields, "Ok", "#ff0000");
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
