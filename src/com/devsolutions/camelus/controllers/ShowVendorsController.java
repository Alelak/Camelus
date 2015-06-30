package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.VendorManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowVendorsController implements Initializable {

	@FXML
	private TableView<Vendor> vendorTableView;
	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button showButton;

	@FXML
	private Pane rightSearchPane;
	@FXML
	private Pane leftSearchPane;

	@FXML
	private TextField searchField;

	@FXML
	private TableColumn<Vendor, String> vendorFirstNameCol;
	@FXML
	private TableColumn<Vendor, String> vendorFLastNameCol;
	@FXML
	private TableColumn<Vendor, String> vendorIdCol;
	@FXML
	private TableColumn<Vendor, String> vendorLoginCol;

	private List<Vendor> vendorsList;
	private ObservableList<Vendor> vendorsObservableList;
	private SortedList<Vendor> sortedData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initTableView();

		FilteredList<Vendor> filteredData = new FilteredList<>(
				vendorsObservableList, p -> true);

		searchField.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(vendor -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}

						String lowerCaseFilter = newValue.toLowerCase();
						String id = vendor.getId() + "";

						if (vendor.getFname().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (vendor.getLname().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (vendor.getLogin().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (id.contains(lowerCaseFilter)) {
							return true;
						}

						return false;
					});
				});

		sortedData = new SortedList<>(filteredData);

		sortedData.comparatorProperty().bind(
				vendorTableView.comparatorProperty());

		vendorTableView.setItems(sortedData);

		addButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/addupdatevendor.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				AddUpdateVendorController controller = loader
						.<AddUpdateVendorController> getController();
				controller.initData(this, "Ajouter", null, -1);

				newStage.initModality(Modality.APPLICATION_MODAL);

				newStage.show();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		deleteButton.setOnAction(e -> {
			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			if (vendor != null) {
				VendorManager.delete(vendor.getId());
				removeFromTableView(vendor);
			}

		});

		editButton.setOnAction(e -> {

			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			int index = vendorTableView.getSelectionModel().getSelectedIndex();
			if (vendor != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"../views/addupdatevendor.fxml"));

					Stage newStage = new Stage();
					Scene scene;

					scene = new Scene(loader.load());
					newStage.setScene(scene);

					AddUpdateVendorController controller = loader
							.<AddUpdateVendorController> getController();
					controller.initData(this, "Modifier", vendor, index);

					newStage.initModality(Modality.APPLICATION_MODAL);

					newStage.show();

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		showButton.setOnAction(e -> {
			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			if (vendor != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"../views/showvendordetails.fxml"));

					Stage newStage = new Stage();
					Scene scene;

					scene = new Scene(loader.load());
					newStage.setScene(scene);

					ShowVendorDetailsController controller = loader
							.<ShowVendorDetailsController> getController();
					controller.initData(vendor);

					newStage.initModality(Modality.APPLICATION_MODAL);

					newStage.show();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		vendorTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						deleteButton.setDisable(false);
						editButton.setDisable(false);
						showButton.setDisable(false);
					} else {
						deleteButton.setDisable(true);
						editButton.setDisable(true);
						showButton.setDisable(true);
					}
				});
	}

	public void initTableView() {
		vendorsList = VendorManager.getAll();
		vendorsObservableList = FXCollections.observableArrayList();

		vendorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		vendorFirstNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"fname"));
		vendorFLastNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"lname"));
		vendorLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));

		vendorsObservableList.addAll(vendorsList);
	}

	public void addToTableView(Vendor vendor) {
		vendorsObservableList.add(vendor);
	}

	public void removeFromTableView(Vendor vendor) {
		vendorsObservableList.remove(vendor);
	}

	public void updateTableView(int index, Vendor vendor) {
		vendorsObservableList.set(index, vendor);
	}

	public TableView<Vendor> getTable() {
		TableView<Vendor> table = vendorTableView;
		return table;
	}
}
