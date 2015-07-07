package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.managers.AdminManager;
import com.devsolutions.camelus.utils.CRUD;

public class ShowAdminsController implements Initializable {

	@FXML
	private TableView<Admin> adminsTableView;
	@FXML
	private TableColumn<Admin, Integer> idColumn;
	@FXML
	private TableColumn<Admin, String> fullnameColumn;
	@FXML
	private TableColumn<Admin, String> loginColumn;
	@FXML
	private TableColumn<Admin, Date> hireDateColumn;
	@FXML
	private Button addBtn;
	@FXML
	private Button updateBtn;
	@FXML
	private Button deleteBtn;
	@FXML
	private Button showDetailsBtn;
	private ObservableList<Admin> adminsOb;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<Admin, Integer>(
				"id"));
		fullnameColumn.setCellValueFactory(new PropertyValueFactory<>(
				"full_name"));
		loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		hireDateColumn.setCellValueFactory(new PropertyValueFactory<>(
				"hire_date"));
		hireDateColumn.setCellFactory(column -> {
			return new TableCell<Admin, Date>() {
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(simpleDateFormat.format(item));
					}
				}
			};
		});
		adminsOb = FXCollections.observableArrayList(AdminManager.getAll());
		adminsTableView.setItems(adminsOb);
		addBtn.setOnAction(e -> {
			showAddUpdateWindow(null, -1, CRUD.CREATE);
		});

		adminsTableView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Admin>() {

					@Override
					public void changed(
							ObservableValue<? extends Admin> observable,
							Admin oldValue, Admin newValue) {
						if (newValue != null) {
							updateBtn.setDisable(false);
							deleteBtn.setDisable(false);
							showDetailsBtn.setDisable(false);
						} else {
							updateBtn.setDisable(true);
							deleteBtn.setDisable(true);
							showDetailsBtn.setDisable(true);
						}
					}

				});

		updateBtn.setOnAction(e -> {
			showAddUpdateWindow(adminsTableView.getSelectionModel()
					.getSelectedItem(), adminsTableView.getSelectionModel()
					.getSelectedIndex(), CRUD.UPDATE);
		});
		deleteBtn.setOnAction(e -> {
			Admin adminTodelete = adminsTableView.getSelectionModel()
					.getSelectedItem();
			if (adminTodelete.getSuper_admin() == 0) {
				adminsOb.remove(adminTodelete);
				AdminManager.delete(adminTodelete.getId());
			}

		});
		showDetailsBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/showadmindetails.fxml"));

				Stage newStage = new Stage();
				Scene scene = new Scene(loader.load());
				ShowAdminController showAdminController = loader
						.<ShowAdminController> getController();
				showAdminController.initData(adminsTableView
						.getSelectionModel().getSelectedItem());
				newStage.setScene(scene);
				newStage.initStyle(StageStyle.UNDECORATED);
				newStage.initOwner(addBtn.getScene().getWindow());
				newStage.initModality(Modality.APPLICATION_MODAL);
				newStage.show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

	}

	public void addToTable(Admin admin) {
		adminsOb.add(admin);
	}

	public void updateTable(int index, Admin admin) {
		adminsOb.set(index, admin);
	}

	private void showAddUpdateWindow(Admin adminToupdate, int index, CRUD type) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"../views/addupdateadmin.fxml"));

			Stage newStage = new Stage();
			Scene scene = new Scene(loader.load());
			AddUpdateAdminController addUpdateAdminController = loader
					.<AddUpdateAdminController> getController();
			addUpdateAdminController.initStageAndData(this, adminToupdate,
					index, type);
			newStage.setScene(scene);
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.initOwner(addBtn.getScene().getWindow());
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
