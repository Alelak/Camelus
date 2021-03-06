package com.devsolutions.camelus.views;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.AdminManager;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.Choice;
import com.devsolutions.camelus.utils.FXUtils;

public class AddUpdateVendorController implements Initializable {

	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Label titleWindow;

	@FXML
	private TextField textUsername;
	@FXML
	private PasswordField textPassword;
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
		FXUtils.addDraggableNode(titleBar);
		commissions = CommissionManager.getAll();

		commissionChoices = FXCollections.observableArrayList();
		commissionChoices.add(new Choice(0, "Choisir"));

		for (Commission commission : commissions) {
			if (commission.getType() == 0)
				commissionChoices.add(new Choice(commission.getId(), commission
						.getRate()
						+ "% (>="
						+ commission.getMcondition()
						+ "$)"));
			else
				commissionChoices.add(new Choice(commission.getId(), commission
						.getRate()
						+ "$ (>="
						+ commission.getMcondition()
						+ "$)"));
		}

		commission.setItems(commissionChoices);
		commission.getSelectionModel().select(0);

		textSin.setOnKeyReleased(e -> {
			if (textSin.getText().matches(
					"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
				textSin.setStyle(FXUtils.HAS_SUCCESS);

			} else
				textSin.setStyle(FXUtils.HAS_ERROR);
		});

		textSin.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (!newPropertyValue) {
					if (textSin.getText().isEmpty())
						textSin.setStyle("-fx-border-width: 0;");
					if (textSin.getText().matches(
							"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
						textSin.setStyle("-fx-border-width: 0;");
					}
				}
			}
		});

		btn.setOnAction(e -> {
			String username = textUsername.getText().trim();
			String password = textPassword.getText().trim();
			String sin = textSin.getText().trim();
			String fname = textFname.getText().trim();
			String lname = textLname.getText().trim();

			String invalidFields = "";
			boolean validfields = true;
			if (vendorToUpdate != null) {
				if (fname.isEmpty() || lname.isEmpty() || username.isEmpty()
						|| sin.isEmpty()) {
					invalidFields += "Tous les champs doivent étre remplis \n";
					validfields = false;
				}

				if (username.isEmpty()) {
					validfields = false;
				} else {
					Vendor vendorByLogin = VendorManager
							.getByUserName(username);
					if (vendorByLogin != null
							&& !vendorByLogin.getLogin().equals(
									vendorToUpdate.getLogin())) {
						validfields = false;
						invalidFields += "Ce nom d'utilisateur a était déja choisie \n";
					}

				}

				if (!sin.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
					invalidFields += "Le NAS doit etre un nombre de 9 chiffres \n";
					validfields = false;
				} else {

					Vendor vendorBySin = VendorManager.getBySin(sin);
					Admin adminBySin = AdminManager.getBySin(sin);
					if (vendorBySin != null
							&& !vendorToUpdate.getSin().equals(
									vendorBySin.getSin())) {
						invalidFields += "Ce NAS existe déja, veuillez saisir un NAS valide. \n";
						validfields = false;
					}
					if (adminBySin != null
							&& !vendorToUpdate.getSin().equals(
									adminBySin.getSin())) {
						invalidFields += "Ce NAS existe déja, veuillez saisir un NAS valide. \n";
						validfields = false;
					}

				}
				if (!password.isEmpty()) {
					if (password.length() < 8) {
						validfields = false;
						invalidFields += "Mot de passe de 8 caracteres et plus. \n";
					}
				}
			} else {
				if (fname.isEmpty() || lname.isEmpty() || username.isEmpty()
						|| sin.isEmpty() || password.isEmpty()) {
					invalidFields += "Tous les champs doivent étre remplis \n";
					validfields = false;
				}
				if (password.length() < 8) {
					validfields = false;
					invalidFields += "Mot de passe de 8 caracteres et plus. \n";
				}
				if (username.isEmpty()) {
					validfields = false;
				} else if (VendorManager.getByUserName(username) != null) {
					validfields = false;
					invalidFields += "Ce nom d'utilisateur a était déja choisie \n";
				}
				if (!sin.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
					invalidFields += "Le NAS doit etre un nombre de 9 chiffres \n";
					validfields = false;
				} else if (VendorManager.getBySin(sin) != null
						|| AdminManager.getBySin(sin) != null) {
					validfields = false;
					invalidFields += "Ce NAS existe déja, veuillez saisir un NAS valide. \n";
				}
			}
			if (username.length() > 16) {
				validfields = false;
				invalidFields += "Le nom d'utilisateur ne doit pas depasse 16 caracteres \n";
			}
			if (fname.length() > 45) {
				invalidFields += "Le prenom ne doit pas depasse 45 caracteres\n";
				validfields = false;

			}
			if (lname.length() > 45) {
				invalidFields += "Le nom ne doit pas depasse 45 caracteres\n";
				validfields = false;

			}
			if (hireDate.getValue() == null) {
				invalidFields += "Vous devez choisir une date d'embauche \n";
				validfields = false;
			}

			if (commission.getSelectionModel().getSelectedIndex() == 0) {
				invalidFields += "Vous devez choisir une commission \n";
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

				vendor.setLogin(username);

				vendor.setFname(fname);
				vendor.setLname(lname);
				vendor.setSin(sin);
				vendor.setCommission_id(commission.getSelectionModel()
						.getSelectedItem().getId());
				vendor.setHire_date(date);

				if (vendorToUpdate == null) {
					vendor.setPassword(password);
					VendorManager.add(vendor);
					vendorTVConroller.addToTableView(vendor);
					vendorTVConroller.showTableView();
				} else {
					if (password.isEmpty()) {
						vendor.setPassword(vendorToUpdate.getPassword());
					} else {
						vendor.setPassword(DigestUtils.sha1Hex(password));
					}

					vendor.setId(vendorToUpdate.getId());
					VendorManager.update(vendor);
					vendorTVConroller.updateTableView(index, vendor);
				}

				stage.close();

			} else {
				try {
					new CustomInfoBox(stage, BoxType.INFORMATION,
							invalidFields, "Ok");
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
		textFname.setText(vendorToUpdate.getFname());
		textLname.setText(vendorToUpdate.getLname());

		hireDate.setValue(vendorToUpdate.getHire_date().toInstant()
				.atZone(ZoneId.systemDefault()).toLocalDate());

		textSin.setText(String.valueOf(vendorToUpdate.getSin()));
		commission.getSelectionModel()
				.select(vendorToUpdate.getCommission_id());
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}

	public void setTitleWindow(String title) {
		titleWindow.setText(title);
	}
}
