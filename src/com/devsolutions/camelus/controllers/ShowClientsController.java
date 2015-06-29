package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.services.Session;

public class ShowClientsController implements Initializable {
	@FXML
	private TableView<Client> clientsTableView;
	
	@FXML
	private Button addBtn;
	
	@FXML
	private Button editBtn;
	
	@FXML
	private Button deleteBtn;
	
	@FXML
	private Button showBtn;

	private List<Client> clientsList;
	private ObservableList<Client> clientsObservableList;

	private TableColumn<Client, String> clientIdCol;
	private TableColumn<Client, String> enterpriseNameCol;
	private TableColumn<Client, String> contactNameCol;
	private TableColumn<Client, String> contactTelCol;
	private TableColumn<Client, String> contactEmailCol;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		clientsTableView.getColumns().addAll(clientIdCol, enterpriseNameCol,
				contactNameCol, contactTelCol, contactEmailCol);
		clientsTableView.setPlaceholder(new Label("Pas de Client"));
		
		clientsTableView.getSelectionModel().selectedItemProperty()
		.addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				showBtn.setDisable(false);
				deleteBtn.setDisable(false);
				editBtn.setDisable(false);
			} else {
				showBtn.setDisable(true);
				deleteBtn.setDisable(true);
				editBtn.setDisable(true);
			}
		});
		
		
		addBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/addupdateclient.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				/*AddUpdateVendorController controller = loader
						.<AddUpdateVendorController> getController();
				controller.initData(this, "Ajouter", null, -1);*/

				newStage.initModality(Modality.APPLICATION_MODAL);

				newStage.show();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		
		editBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/addupdateclient.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				/*AddUpdateVendorController controller = loader
						.<AddUpdateVendorController> getController();
				controller.initData(this, "Ajouter", null, -1);*/

				newStage.initModality(Modality.APPLICATION_MODAL);

				newStage.show();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	public void initTableView() {
		if (Session.vendor != null) {
			clientsList = ClientManager.getByVendorId(Session.vendor.getId());
		} else {
			clientsList = ClientManager.getAll();
		}
		clientsObservableList = FXCollections.observableArrayList();

		clientIdCol = new TableColumn<Client, String>("Id");
		clientIdCol.setMinWidth(100);
		clientIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		enterpriseNameCol = new TableColumn<Client, String>("Entreprise");
		enterpriseNameCol.setMinWidth(100);
		enterpriseNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"enterprise_name"));

		contactNameCol = new TableColumn<Client, String>("Nom");
		contactNameCol.setMinWidth(100);
		contactNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"contact_name"));

		contactTelCol = new TableColumn<Client, String>("T�l�phone");
		contactTelCol.setMinWidth(200);
		contactTelCol.setCellValueFactory(new PropertyValueFactory<>(
				"contact_tel"));

		contactEmailCol = new TableColumn<Client, String>("Email");
		contactEmailCol.setMinWidth(200);
		contactEmailCol.setCellValueFactory(new PropertyValueFactory<>(
				"contact_email"));

		clientsObservableList.addAll(clientsList);

		clientsTableView.setItems(clientsObservableList);
	}
}
