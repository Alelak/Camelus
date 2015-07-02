package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.CustomDialogBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ShowClientsController implements Initializable {
	@FXML
	private GridPane motherGrid;
	@FXML
	private GridPane content;

	@FXML
	private RowConstraints rowOne;
	@FXML
	private VBox gridRowOne;
	@FXML
	private Label message1;

	@FXML
	private RowConstraints rowTwo;
	@FXML
	private GridPane gridRowTwo;
	@FXML
	private TextField textFieldSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnRefresh;

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

	private ObservableList<Client> ClientsOb;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientsOb = FXCollections.observableArrayList(ClientManager.getAll());
		// System.out.println(ClientsOb.size());
		if (ClientsOb.size() == 0) {
			noDataToShow();
		} else {
			initTableView();
			clientTableView.setItems(ClientsOb);
			showTableView();
		}

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
						} else {
							btnUpdate.setDisable(true);
							btnDelete.setDisable(true);
							btnConsult.setDisable(true);
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
						"Voulez vous vraiment supprimer " + clientTableView
						.getSelectionModel().getSelectedItem().getContact_name() + " de votre liste de clients?", "Oui", "Non");
				customDialogBox.positiveButton
						.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Client clientTodelete = clientTableView.getSelectionModel()
										.getSelectedItem();
								ClientsOb.remove(clientTodelete);
								ClientManager.delete(clientTodelete.getId());
								Stage dialogBoxStage = (Stage) customDialogBox.positiveButton.getScene().getWindow();
								dialogBoxStage.close();
							}
						});
				
				customDialogBox.negativeButton
				.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Stage dialogBoxStage = (Stage) customDialogBox.positiveButton.getScene().getWindow();
						dialogBoxStage.close();

					}
				});
			} catch (IOException e2) {
				e2.printStackTrace();
			}		
		});
	}

	
	public void addToTable(Client client) {
		ClientsOb.add(client);
	}

	public void updateTable(int index,Client client) {
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
		content.getChildren().remove(gridRowTwo);
		rowOne.setPercentHeight(100);
		rowTwo.setPercentHeight(0);
	}

	public void showTableView() {
		content.getChildren().remove(gridRowOne);
		// content.getChildren().add(gridRowTwo);
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
		// controller.initData(this);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(motherGrid.getScene().getWindow());
		stage.show();
	}
}
