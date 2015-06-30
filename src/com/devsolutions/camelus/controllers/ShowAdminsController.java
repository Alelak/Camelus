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
	private TableColumn<Admin, String> fnameColumn;
	@FXML
	private TableColumn<Admin, String> lnameColumn;
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
		fnameColumn.setCellValueFactory(new PropertyValueFactory<>("fname"));
		lnameColumn.setCellValueFactory(new PropertyValueFactory<>("lname"));
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

		adminsTableView.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(
							ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue) {
						updateBtn.setDisable(false);
						deleteBtn.setDisable(false);
						showDetailsBtn.setDisable(false);
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
			adminsOb.remove(adminTodelete);
			AdminManager.delete(adminTodelete.getId());

		});
		showDetailsBtn.setOnAction(e -> {
			showAddUpdateWindow(adminsTableView.getSelectionModel()
					.getSelectedItem(), adminsTableView.getSelectionModel()
					.getSelectedIndex(), CRUD.READ);
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
