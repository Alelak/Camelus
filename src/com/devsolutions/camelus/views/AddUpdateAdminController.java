package com.devsolutions.camelus.views;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;


import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.AdminManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.FXUtils;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddUpdateAdminController implements Initializable {
	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Button btnAddUpdate;
	@FXML
	private Button btnCancel;
	@FXML
	private TextField logintxt;
	@FXML
	private PasswordField passwordtxt;
	@FXML
	private TextField lnametxt;
	@FXML
	private TextField fnametxt;
	@FXML
	private TextField sintxt;
	@FXML
	private DatePicker hiredatetxt;
	@FXML
	private Label titleWindow;
	private ShowAdminsController showAdminsController;
	private Admin adminToUpdate;
	private int index;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);

		lblClose.setOnMouseClicked(e -> {
			stage = (Stage) lblClose.getScene().getWindow();
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
					String login = logintxt.getText().trim();
					String password = passwordtxt.getText().trim();
					String lname = lnametxt.getText().trim();
					String fname = fnametxt.getText().trim();
					String sin = sintxt.getText().trim();
					String date = null;
					if (hiredatetxt.getValue() != null) {
						date = hiredatetxt.getValue().toString();
					}
					if (login.isEmpty() || password.isEmpty()
							|| lname.isEmpty() || fname.isEmpty()
							|| sin.isEmpty()) {
						valid = false;
						feedbackmsg += "Tous les champs avec une étoile sont requis.\n";
					}
					if (password.length() < 8) {
						valid = false;
						feedbackmsg += "Mot de passe de 8 caracteres et plus. \n";
					}
					if (adminToUpdate != null) {
						if (login.isEmpty()) {
							valid = false;
						} else {
							Admin adminByLogin = AdminManager
									.getByUserName(login);
							if (adminByLogin != null
									&& !adminToUpdate.getLogin().equals(
											adminByLogin.getLogin())) {
								feedbackmsg += "Ce nom d'utilisateur a était deja choisie. \n";
								valid = false;
							}
						}

						if (!sin.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
							valid = false;
							feedbackmsg += "Le NAS doit etre un nombre de 9 chiffres. \n";
						} else {
							Vendor vendorBySin = VendorManager.getBySin(sin);
							Admin adminBySin = AdminManager.getBySin(sin);
							if (vendorBySin != null
									&& !adminToUpdate.getSin().equals(
											vendorBySin.getSin())) {
								feedbackmsg += "Ce NAS existe déja, veuillez saisir un NAS valide. \n";
								valid = false;
							}
							if (adminBySin != null
									&& !adminToUpdate.getSin().equals(
											adminBySin.getSin())) {
								feedbackmsg += "Ce NAS existe déja, veuillez saisir un NAS valide. \n";
								valid = false;
							}

						}

					} else {

						if (login.isEmpty()) {
							valid = false;
						} else if (AdminManager.getByUserName(login) != null) {
							feedbackmsg += "Ce nom d'utilisateur a était déja choisie. \n";
							valid = false;

						}

						if (!sin.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
							valid = false;
							feedbackmsg += "Le NAS doit etre un nombre de 9 chiffres. \n";
						} else if (VendorManager.getBySin(sin) != null
								|| AdminManager.getBySin(sin) != null) {
							feedbackmsg += "Ce NAS existe déja, veuillez saisir un NAS valide. \n";
							valid = false;

						}
					}
					if (valid) {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						Admin admin = new Admin();
						admin.setLogin(login);
						admin.setPassword(password);
						admin.setFname(fname);
						admin.setLname(lname);
						admin.setSin(sin);
						if (date != null) {
							try {
								admin.setHire_date(simpleDateFormat.parse(date));
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							admin.setHire_date(new Date());
						}
						if (adminToUpdate != null) {
							admin.setId(adminToUpdate.getId());
							AdminManager.update(admin);
							showAdminsController.updateTable(index, admin);
						} else {
							AdminManager.add(admin);
							showAdminsController.addToTable(admin);
						}
						stage.close();
					} else {
						try {
							new CustomInfoBox(stage,BoxType.INFORMATION, feedbackmsg, "Ok");
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				});
	}

	public void initStageAndData(ShowAdminsController showAdminsController,
			Admin admin, int index, CRUD type) {
		this.showAdminsController = showAdminsController;
		switch (type) {
		case CREATE:
			titleWindow.setText("Ajouter Admin");
			break;
		case UPDATE:
			titleWindow.setText("Modifier Admin");
			btnAddUpdate.setText("Modifier");
			this.adminToUpdate = admin;
			this.index = index;
			setData(adminToUpdate);
			break;
		default:
			titleWindow.setText("Default");
			break;
		}
	}

	private void setData(Admin admin) {
		logintxt.setText(admin.getLogin());
		passwordtxt.setText(admin.getPassword());
		lnametxt.setText(admin.getLname());
		fnametxt.setText(admin.getFname());
		sintxt.setText(admin.getSin());
		hiredatetxt.setValue(admin.getHire_date().toInstant()
				.atZone(ZoneId.systemDefault()).toLocalDate());
	}
}
