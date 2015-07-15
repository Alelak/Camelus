package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.Date;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.managers.AdminManager;
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.CRUD;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;

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
	private Button btnRefresh;
	@FXML
	private Button showDetailsBtn;
	@FXML
	private TextField textFieldSearch;
	private ObservableList<Admin> adminsOb;
	private SortedList<Admin> sortedData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<Admin, Integer>(
				"id"));
		fullnameColumn.setCellValueFactory(new PropertyValueFactory<>(
				"full_name"));
		loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
		hireDateColumn.setCellValueFactory(new PropertyValueFactory<>(
				"hire_date"));
		hireDateColumn.setCellFactory(column -> {
			return new TableCell<Admin, Date>() {
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(StringUtils.formatDate(item));
					} else {
						setText("");
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

		btnRefresh.setOnAction(e -> {
			adminsOb.clear();
			adminsOb.addAll(AdminManager.getAll());
		});

		updateBtn.setOnAction(e -> {
			showAddUpdateWindow(adminsTableView.getSelectionModel()
					.getSelectedItem(), adminsTableView.getSelectionModel()
					.getSelectedIndex(), CRUD.UPDATE);
		});

		FilteredList<Admin> filteredData = new FilteredList<>(adminsOb,
				p -> true);
		textFieldSearch.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(Admin -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}
						String lowerCaseFilter = newValue.toLowerCase();
						String id = Admin.getId() + "";
						if (Admin.getLogin().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (Admin.getFull_name().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (Admin.getHire_date().toString()
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
				adminsTableView.comparatorProperty());
		adminsTableView.setItems(sortedData);

		deleteBtn.setOnAction(e -> {
			Stage stage = (Stage) deleteBtn.getScene().getWindow();
			Admin adminTodelete = adminsTableView.getSelectionModel()
					.getSelectedItem();
			if (adminTodelete.getSuper_admin() == 0) {

				try {
					CustomDialogBox dialogBox = new CustomDialogBox(stage,
							BoxType.WARNING,
							"Voulez vous vraiment supprimer ce admin?", "Oui",
							"Non");
					dialogBox.positiveButton
							.setOnAction(new EventHandler<ActionEvent>() {

								@Override
								public void handle(ActionEvent event) {
									adminsOb.remove(adminTodelete);
									AdminManager.delete(adminTodelete.getId());
									adminsTableView.getSelectionModel()
											.clearSelection();
									dialogBox.stage.close();
								}

							});

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			} else {
				try {
					new CustomInfoBox(stage, BoxType.ERROR,
							"Impossible de supprimer le super admin", "Ok");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});
		showDetailsBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"ShowAdminDetails.fxml"));

				Stage newStage = new Stage();
				Scene scene = new Scene(loader.load());
				scene.getStylesheets().add(
						getClass().getResource("childWindow.css")
								.toExternalForm());
				ShowAdminController showAdminController = loader
						.<ShowAdminController> getController();
				showAdminController.initData(adminsTableView
						.getSelectionModel().getSelectedItem());
				newStage.setScene(scene);
				newStage.initStyle(StageStyle.UNDECORATED);
				newStage.initOwner(addBtn.getScene().getWindow());
				newStage.initModality(Modality.APPLICATION_MODAL);
				newStage.show();
				FXUtils.centerStage((Stage) addBtn.getScene().getWindow(),
						newStage, 22);
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
					"AddUpdateAdmin.fxml"));

			Stage newStage = new Stage();
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(
					getClass().getResource("childWindow.css").toExternalForm());
			AddUpdateAdminController addUpdateAdminController = loader
					.<AddUpdateAdminController> getController();
			addUpdateAdminController.initStageAndData(this, adminToupdate,
					index, type);
			newStage.setScene(scene);
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.initOwner(addBtn.getScene().getWindow());
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.show();
			FXUtils.centerStage((Stage) addBtn.getScene().getWindow(),
					newStage, 22);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
