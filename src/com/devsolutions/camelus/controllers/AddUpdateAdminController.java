package com.devsolutions.camelus.controllers;

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
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.CustomInfoBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddUpdateAdminController implements Initializable {

	@FXML
	private Parent root;
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
	private double initialX;
	private double initialY;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addDraggableNode(titleBar);

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
					Admin adminByLogin = AdminManager.getByUserName(login);
					Vendor vendorBySin = VendorManager.getBySin(sin);
					Admin adminBySin = AdminManager.getBySin(sin);
					if (hiredatetxt.getValue() != null) {
						date = hiredatetxt.getValue().toString();
					}
					if (login.isEmpty() || password.isEmpty()
							|| lname.isEmpty() || fname.isEmpty()
							|| sin.isEmpty()) {
						valid = false;
						feedbackmsg += "Tous les champs avec une etoile sont requis.\n";
					}
					if (password.length() < 8) {
						valid = false;
						feedbackmsg += "Mot de passe de 8 caracteres et plus. \n";
					}
					if (adminToUpdate != null) {
						if (login.isEmpty()) {
							valid = false;
						} else if (adminByLogin != null
								&& !adminByLogin.getLogin().equals(
										adminToUpdate.getLogin())) {
							feedbackmsg += "Ce nom d'utilisateur a etait deja choisie. \n";
							valid = false;
						}
						if (!sin.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
							valid = false;

							feedbackmsg += "Le NAS doit etre un nombre de 9 chiffres. \n";
						} else if (adminBySin != null
								&& vendorBySin != null
								&& !adminBySin.getSin().equals(
										adminToUpdate.getSin())
								&& !vendorBySin.getSin().equals(
										adminToUpdate.getSin())) {
							feedbackmsg += "Ce NAS existe deja, veuillez saisir un NAS valide. \n";
							valid = false;

						}
					} else {
						if (login.isEmpty()) {
							valid = false;
						} else if (adminByLogin != null) {
							feedbackmsg += "Ce nom d'utilisateur a etait deja choisie. \n";
							valid = false;
						}
						if (!sin.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
							valid = false;

							feedbackmsg += "Le NAS doit etre un nombre de 9 chiffres. \n";
						} else if (adminBySin != null && vendorBySin != null) {
							feedbackmsg += "Ce NAS existe deja, veuillez saisir un NAS valide. \n";
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
