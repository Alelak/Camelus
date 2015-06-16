package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.utils.Choice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddVendorController implements Initializable {
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
	private Button btnAdd;
	@FXML
	private Button btnCancel;

	private List<Commission> commissions;
	private ObservableList<Choice> commissionChoices;

	private ShowVendorsController vendorTVConroller;

	private Stage stage;

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

		btnAdd.setOnAction(e -> {
			int sin = -1;

			if (textSin.getText().matches(
					"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))
				sin = Integer.parseInt(textSin.getText());
			stage = (Stage) btnCancel.getScene().getWindow();

			if (!textUsername.getText().isEmpty()
					&& !textPassword.getText().isEmpty()
					&& !textFname.getText().isEmpty()
					&& !textLname.getText().isEmpty()
					&& !textSin.getText().isEmpty() && sin >= 0
					&& hireDate.getValue() != null
					&& commission.getSelectionModel().getSelectedIndex() != 0) {
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
				vendor.setSin(sin);
				vendor.setCommission_id(commission.getSelectionModel()
						.getSelectedIndex());
				vendor.setHire_date(date);
				VendorManager.add(vendor);
				vendorTVConroller.addToTableView(vendor);
				stage.close();

			} else {
				System.out.println("Tous les champs doivent être remplis");
			}
		});

		btnCancel.setOnAction(e -> {
			stage = (Stage) btnCancel.getScene().getWindow();
			stage.close();
		});
	}

	public void initData(ShowVendorsController vendorTVConroller) {
		this.vendorTVConroller = vendorTVConroller;
	}

}
