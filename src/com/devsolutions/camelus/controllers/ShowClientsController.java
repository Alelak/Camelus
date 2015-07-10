package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.CustomDialogBox;
import com.devsolutions.camelus.utils.FXUtils;

public class ShowClientsController implements Initializable {
	@FXML
	private GridPane motherGrid;
	@FXML
	private GridPane content;
	@FXML
	private GridPane gridRowTwo;
	@FXML
	private RowConstraints rowOne;
	@FXML
	private VBox gridRowOne;
	@FXML
	private Label message1;
	@FXML
	private RowConstraints rowTwo;

	@FXML
	private TextField textFieldSearch;

	@FXML
	private TableView<Client> clientTableView;
	@FXML
	private TableColumn<Client, Long> idColumn;
	@FXML
	private TableColumn<Client, String> enterprise_name_Column;
	@FXML
	private TableColumn<Client, String> contact_name_Column;
	@FXML
	private TableColumn<Client, String> contact_tel_Column;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnUpdate;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnConsult;
	@FXML
	private Button btnOrder;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnRefresh;

	private ObservableList<Client> ClientsOb;
	private SortedList<Client> sortedData;
	private MainWindowController mainWindowController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(false);
		ClientsOb = FXCollections.observableArrayList(ClientManager.getAll());

		if (ClientsOb.size() == 0) {

			noDataToShow();
		} else {
			showTableView();
		}
		initTableView();
		clientTableView.setItems(ClientsOb);

		clientTableView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Client>() {
					@Override
					public void changed(
							ObservableValue<? extends Client> observable,
							Client oldValue, Client newValue) {
						if (newValue != null) {

							btnUpdate.setDisable(false);
							btnDelete.setDisable(false);
							btnConsult.setDisable(false);
							btnOrder.setDisable(false);
						} else {
							btnUpdate.setDisable(true);
							btnDelete.setDisable(true);
							btnConsult.setDisable(true);
							btnOrder.setDisable(true);
						}
					}
				});
		btnAdd.setOnAction(e -> showAddUpdateClientWindow(null, -1, CRUD.CREATE));
		btnUpdate.setOnAction(e -> showAddUpdateClientWindow(clientTableView
				.getSelectionModel().getSelectedItem(), clientTableView
				.getSelectionModel().getSelectedIndex(), CRUD.UPDATE));
		btnDelete.setOnAction(e -> {
			try {
				CustomDialogBox customDialogBox = new CustomDialogBox(
						(Stage) btnDelete.getScene().getWindow(),
						"Voulez vous vraiment supprimer "
								+ clientTableView.getSelectionModel()
										.getSelectedItem().getContact_name()
								+ " de votre liste de clients?", "Oui", "Non");

				customDialogBox.positiveButton
						.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Client clientTodelete = clientTableView
										.getSelectionModel().getSelectedItem();

								ClientsOb.remove(clientTodelete);
								ClientManager.delete(clientTodelete.getId());
								if (ClientsOb.isEmpty()) {
									noDataToShow();
								}
								Stage dialogBoxStage = (Stage) customDialogBox.positiveButton
										.getScene().getWindow();
								dialogBoxStage.close();
							}
						});

				customDialogBox.negativeButton
						.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Stage dialogBoxStage = (Stage) customDialogBox.positiveButton
										.getScene().getWindow();
								dialogBoxStage.close();

							}
						});
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});

		btnConsult.setOnAction(e -> {

			Client clientToShow = clientTableView.getSelectionModel()
					.getSelectedItem();
			showConsultClientWindow(clientToShow);
		});

		FilteredList<Client> filteredData = new FilteredList<>(ClientsOb,
				p -> true);
		textFieldSearch.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(Client -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}
						String lowerCaseFilter = newValue.toLowerCase();
						String id = Client.getId() + "";
						if (Client.getContact_name().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (Client.getEnterprise_name().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (Client.getContact_tel().toLowerCase()
								.toLowerCase().contains(lowerCaseFilter)) {
							return true;
						} else if (id.contains(lowerCaseFilter)) {
							return true;
						}
						return false;
					});
				});

		sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(
				clientTableView.comparatorProperty());
		clientTableView.setItems(sortedData);

		btnRefresh.setOnAction(e -> {
			ClientsOb.clear();
			ClientsOb.addAll(ClientManager.getAll());
		});

		btnOrder.setOnAction(e -> {
			Client selectedClient = clientTableView.getSelectionModel()
					.getSelectedItem();
			mainWindowController.switchScene("showorders", selectedClient);
		});

	}

	public void selectLastRow() {
		int indexLastRow = ClientsOb.size() - 1;
		clientTableView.getSelectionModel().select(indexLastRow);
		clientTableView.getFocusModel().focus(indexLastRow);
	}

	public void selectTheModifierRow(int index) {
		clientTableView.getSelectionModel().select(index);
		clientTableView.getFocusModel().focus(index);
	}

	public void addToTable(Client client) {
		ClientsOb.add(client);
	}

	public void updateTable(int index, Client client) {
		ClientsOb.set(index, client);
	}

	private void initTableView() {
		idColumn.setCellValueFactory(new PropertyValueFactory<Client, Long>(
				"id"));
		idColumn.setMaxWidth(0);
		idColumn.setMinWidth(0);
		enterprise_name_Column.setCellValueFactory(new PropertyValueFactory<>(
				"enterprise_name"));
		contact_name_Column.setCellValueFactory(new PropertyValueFactory<>(
				"contact_name"));
		contact_tel_Column.setCellValueFactory(new PropertyValueFactory<>(
				"contact_tel"));
	}

	private void noDataToShow() {
		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(true);
		rowOne.setPercentHeight(100);
		rowTwo.setPercentHeight(0);
	}

	public void showTableView() {
		gridRowOne.setVisible(false);
		gridRowTwo.setVisible(true);
		rowOne.setPercentHeight(0);
		rowTwo.setPercentHeight(100);
	}

	private void showAddUpdateClientWindow(Client clientToupdate, int index,
			CRUD type) {
		Stage stage = new Stage();
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../views/AddUpdateClient.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		AddUpdateClientController controller = loader
				.<AddUpdateClientController> getController();
		controller.initStageAndData(this, clientToupdate, index, type);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(motherGrid.getScene().getWindow());
		stage.show();
		Stage parentStage = (Stage) motherGrid.getScene().getWindow();
		FXUtils.centerStage(parentStage, stage, 22);
	}

	private void showConsultClientWindow(Client clientToShow) {
		Stage stage = new Stage();
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../views/ShowClientDetails.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		ShowClientController controller = loader
				.<ShowClientController> getController();
		controller.initData(clientToShow);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(motherGrid.getScene().getWindow());
		stage.show();
		Stage parentStage = (Stage) motherGrid.getScene().getWindow();
		FXUtils.centerStage(parentStage, stage, 22);
	}

	public void initData(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
	}

}
