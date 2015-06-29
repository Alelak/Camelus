package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.managers.OrderManager;
import com.devsolutions.camelus.services.Session;

public class ShowOrdersController implements Initializable {

	@FXML
	private TableView<OrderTV> orderTableView;
	
	@FXML
	private Button takeOrderBtn;
	@FXML
	private Button showOrderBtn;
	
	@FXML
	private Pane leftPane;
	@FXML
	private Pane rightPane;
	
	@FXML
	private TextField searchField;
	
	@FXML
	private TableColumn<OrderTV, String> orderIdCol;
	@FXML
	private TableColumn<OrderTV, String> vendorFNameCol;
	@FXML
	private TableColumn<OrderTV, String> vendorLNameCol;
	@FXML
	private TableColumn<OrderTV, String> clientNameCol;
	@FXML
	private TableColumn<OrderTV, String> commentCol;
	@FXML
	private TableColumn<OrderTV, String> orderedAtCol;

	private List<OrderTV> ordersList;
	private ObservableList<OrderTV> ordersObservableList;
	private SortedList<OrderTV> sortedData;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		
		FilteredList<OrderTV> filteredData = new FilteredList<>(ordersObservableList,
				p -> true);

		searchField.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(orderTV -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}
						
						String lowerCaseFilter = newValue.toLowerCase();
						String id = orderTV.getId() + "";
						if (orderTV.getEnterprise_name().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (id.contains(lowerCaseFilter)) {
							return true;
						}
						return false;
					});
				});
		sortedData = new SortedList<>(filteredData);

		sortedData.comparatorProperty().bind(orderTableView.comparatorProperty());

		orderTableView.setItems(sortedData);

		if(Session.vendor == null){
			takeOrderBtn.setDisable(true);
		}
		else
		{
			leftPane.setPrefSize(100, 0);
			rightPane.setPrefSize(100, 0);
		}
		
		showOrderBtn.setOnAction(e -> {
			OrderTV orderTV = orderTableView.getSelectionModel()
					.getSelectedItem();
			if (orderTV != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"../views/ShowOrderDetails.fxml"));

					Stage newStage = new Stage();
					Scene scene;

					scene = new Scene(loader.load());

					newStage.setScene(scene);

					ShowOrderDetailsController controller = loader
							.<ShowOrderDetailsController> getController();
					controller.initData(orderTV);

					newStage.initModality(Modality.APPLICATION_MODAL);

					newStage.show();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		takeOrderBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/TakeOrderView.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				TakeOrderController controller = loader
						.<TakeOrderController> getController();
				controller.initData(this);

				newStage.initModality(Modality.APPLICATION_MODAL);

				newStage.show();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		orderTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						showOrderBtn.setDisable(false);
					} else {
						showOrderBtn.setDisable(true);
					}
				});
	}

	public void initTableView() {

		if (Session.vendor != null)
			ordersList = OrderManager.getByVendorId(Session.vendor.getId());
		else
			ordersList = OrderManager.getAllTV();

		ordersObservableList = FXCollections.observableArrayList();

		orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		if(Session.admin != null){
			vendorFNameCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
			vendorLNameCol.setCellValueFactory(new PropertyValueFactory<>("lname"));
		}
		else
		{
			orderTableView.getColumns().remove(vendorFNameCol);
			orderTableView.getColumns().remove(vendorLNameCol);
		}
		
		clientNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"enterprise_name"));
		commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
		orderedAtCol.setCellValueFactory(new PropertyValueFactory<>("ordered_at_formated"));

		ordersObservableList.addAll(ordersList);

		//orderTableView.setItems(ordersObservableList);
	}

	public void addToTableView(OrderTV orderTV) {
		
		ordersObservableList.add(orderTV);
	}
}