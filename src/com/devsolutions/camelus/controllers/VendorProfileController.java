package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.CustomInfoBox;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.FontAwesomeIcon;
import com.devsolutions.camelus.utils.FontAwesomeIconView;
import com.devsolutions.camelus.utils.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class VendorProfileController implements Initializable {
	@FXML
	private Label usernameLabel;

	@FXML
	private Label fnameLabel;
	@FXML
	private Label lnameLabel;
	@FXML
	private Label hiredateLabel;
	@FXML
	private Label sinLabel;
	@FXML
	private Label commissionLabel;
	@FXML
	private Label conditionLabel;
	@FXML
	private Label bonusLabel;

	@FXML
	private Label lblClose;

	@FXML
	private Button btnClose;
	@FXML
	private GridPane titleBar;
	@FXML
	private Label passwordLabel;
	@FXML
	private HBox passwordHbox;
	@FXML
	private Label editlbl;
	private PasswordField passwordTxt;
	@FXML
	private FontAwesomeIconView editFont;
	@FXML
	private Button updateBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		passwordTxt = new PasswordField();
		passwordTxt.setPromptText("Mot de passe");
		FXUtils.addDraggableNode(titleBar);
		usernameLabel.setText(Session.vendor.getLogin());
		passwordLabel.setText("********");
		fnameLabel.setText(Session.vendor.getFname());
		lnameLabel.setText(Session.vendor.getLname());
		hiredateLabel.setText(StringUtils.formatDate(Session.vendor
				.getHire_date()));
		sinLabel.setText(Session.vendor.getSin());
		Commission commission = CommissionManager.getById(Session.vendor
				.getCommission_id());

		if (commission.getType() == 0) {
			commissionLabel.setText("Pourcentage");
			bonusLabel.setText(commission.getRate() + "%");
		} else {
			commissionLabel.setText("Fixe");
			bonusLabel.setText(commission.getRate() + " $");
		}
		conditionLabel.setText(commission.getMcondition() + " $");
		editlbl.setOnMouseClicked(e -> {
			passwordTxt.setText("");
			if (editFont.getIcon().equals(FontAwesomeIcon.ICON_EDIT)) {
				passwordHbox.getChildren().set(1, passwordTxt);
				editFont.setIcon(FontAwesomeIcon.ICON_REMOVE);
				updateBtn.setDisable(false);
			} else {
				passwordHbox.getChildren().set(1, passwordLabel);
				editFont.setIcon(FontAwesomeIcon.ICON_EDIT);
				updateBtn.setDisable(true);
			}
		});
		updateBtn.setOnAction(e -> {
			Stage stage = (Stage) updateBtn.getScene().getWindow();
			boolean valid = true;
			String password = passwordTxt.getText().trim();
			String msg = null;
			if (password.isEmpty()) {
				msg = "Il faut saisir un mot de passe";
				valid = false;
			} else if (password.length() < 8) {
				msg = "Mot de passe de 8 caracteres et plus";
				valid = false;
			}
			if (valid) {
				VendorManager.updatePassword(Session.vendor.getId(), password);
				passwordHbox.getChildren().set(1, passwordLabel);
				editFont.setIcon(FontAwesomeIcon.ICON_EDIT);
				updateBtn.setDisable(true);
				passwordTxt.setText("");
			} else {
				try {
					new CustomInfoBox(stage, msg, "Ok");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}

		});
	}

	@FXML
	private void CloseWindow() {
		((Stage) lblClose.getScene().getWindow()).close();
	}
}
