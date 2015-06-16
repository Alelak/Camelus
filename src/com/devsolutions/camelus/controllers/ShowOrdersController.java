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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.OrderManager;
import com.devsolutions.camelus.managers.VendorManager;

public class ShowOrdersController implements Initializable {

	@FXML
	private TableView<OrderTV> orderTableView;
	
	@FXML
	private Button takeOrderBtn;
	
	@FXML
	private Button showOrderBtn;

	private List<OrderTV> ordersList;
	private ObservableList<OrderTV> ordersObservableList;

	private TableColumn<OrderTV, String> orderIdCol;
	private TableColumn<OrderTV, String> clientNameCol;
	private TableColumn<OrderTV, String> commentCol;
	private TableColumn<OrderTV, String> orderedAtCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		orderTableView.getColumns().addAll(orderIdCol, clientNameCol,
				commentCol, orderedAtCol);
		
		
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
	}

	public void initTableView() {
		ordersList = OrderManager.getByVendorId(1);
		ordersObservableList = FXCollections.observableArrayList();

		orderIdCol = new TableColumn<OrderTV, String>("Id");
		orderIdCol.setMinWidth(100);
		orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		clientNameCol = new TableColumn<OrderTV, String>("Nom du Client");
		clientNameCol.setMinWidth(100);
		clientNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"contact_name"));

		commentCol = new TableColumn<OrderTV, String>("Commentaire");
		commentCol.setMinWidth(100);
		commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

		orderedAtCol = new TableColumn<OrderTV, String>("Date de commande");
		orderedAtCol.setMinWidth(200);
		orderedAtCol.setCellValueFactory(new PropertyValueFactory<>(
				"ordered_at"));

		ordersObservableList.addAll(ordersList);

		orderTableView.setItems(ordersObservableList);
	}
}