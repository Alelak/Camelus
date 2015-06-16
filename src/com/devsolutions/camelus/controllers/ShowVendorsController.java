package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.VendorManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowVendorsController implements Initializable {

	@FXML
	TableView<Vendor> vendorTableView;
	@FXML
	Button addButton;
	@FXML
	Button editButton;
	@FXML
	Button deleteButton;
	@FXML
	Button showButton;

	private List<Vendor> vendorsList;
	private ObservableList<Vendor> vendorsObservableList;

	private TableColumn<Vendor, String> vendorFirstNameCol;
	private TableColumn<Vendor, String> vendorFLastNameCol;
	private TableColumn<Vendor, String> vendorIdCol;
	private TableColumn<Vendor, String> vendorLoginCol;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		refreshTableView();
		vendorTableView.getColumns().addAll(vendorIdCol, vendorFirstNameCol,
				vendorFLastNameCol, vendorLoginCol);

		addButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/VendorScene.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				AddVendorController controller = loader
						.<AddVendorController> getController();
				controller.initData(this);

				newStage.initModality(Modality.APPLICATION_MODAL);

				newStage.show();

				refreshTableView();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		deleteButton.setOnAction(e -> {
			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			if (vendor != null) {
				VendorManager.delete(vendor.getId());
				refreshTableView();
			}

		});
		editButton.setOnAction(e -> {

			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			if (vendor != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"../views/VendorEditScene.fxml"));

					Stage newStage = new Stage();
					Scene scene;

					scene = new Scene(loader.load());
					newStage.setScene(scene);

					EditVendorController controller = loader
							.<EditVendorController> getController();
					controller.initData(this, vendor);

					newStage.initModality(Modality.APPLICATION_MODAL);

					newStage.show();

					// refreshTableView();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	})	;
	}

	public void refreshTableView() {
		vendorsList = VendorManager.getAll();
		vendorsObservableList = FXCollections.observableArrayList();

		vendorIdCol = new TableColumn<Vendor, String>("Id");
		vendorIdCol.setMinWidth(100);
		vendorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		vendorFirstNameCol = new TableColumn<Vendor, String>("Prénom");
		vendorFirstNameCol.setMinWidth(100);
		vendorFirstNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"fname"));

		vendorFLastNameCol = new TableColumn<Vendor, String>("Nom");
		vendorFLastNameCol.setMinWidth(100);
		vendorFLastNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"lname"));

		vendorLoginCol = new TableColumn<Vendor, String>("Nom d'utilisateur");
		vendorLoginCol.setMinWidth(200);
		vendorLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));

		for (Vendor vendor : vendorsList) {
			vendorsObservableList.add(vendor);
		}

		vendorTableView.setItems(vendorsObservableList);

	}

}
