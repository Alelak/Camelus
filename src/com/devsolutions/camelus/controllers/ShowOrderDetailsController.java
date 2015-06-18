package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.OrderLineManager;

public class ShowOrderDetailsController implements Initializable{
	@FXML
	private TableView<OrderLineTV> orderLinesTableView;
	
	@FXML
	private Label orderNumberLabel;
	
	@FXML
	private Label orderedAtLabel;
	
	@FXML
	private Label entrepriseNameLabel;
	
	@FXML
	private Label contactNameLabel;
	
	@FXML
	private Label contactTelLabel;
	
	@FXML
	private Label contactEmailLabel;
	
	@FXML
	private Label orderTotalLabel;
	
	@FXML
	private Label vendorCommissionLabel;
	
	@FXML
	private TextArea orderCommentTextArea;
	
	@FXML
	private Button doneBtn;	
	
	private List<OrderLineTV> orderLinesList;
	private ObservableList<OrderLineTV> orderLinesObservableList;

	private TableColumn<OrderLineTV, String> productUpcCol;
	private TableColumn<OrderLineTV, String> productNameCol;
	private TableColumn<OrderLineTV, String> priceCol;
	private TableColumn<OrderLineTV, String> modifiedPriceCol;
	private TableColumn<OrderLineTV, String> quantityCol;
	private TableColumn<OrderLineTV, String> totalCol;
	
	private Client currentClient;
	private OrderTV orderTV;
	private Commission commission;
	
	private Stage stage;
	
	private long product_id;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		orderLinesTableView.getColumns().addAll(productUpcCol, productNameCol,
				priceCol, modifiedPriceCol, quantityCol, totalCol);
		
		
		doneBtn.setOnAction(e -> {
			stage = (Stage) doneBtn.getScene().getWindow();
			stage.close();
		});
	}
	
	public void initTableView() {
		orderLinesObservableList = FXCollections.observableArrayList();
		productUpcCol = new TableColumn<OrderLineTV, String>("UPC");
		productUpcCol.setMinWidth(100);
		productUpcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));

		productNameCol = new TableColumn<OrderLineTV, String>("Nom du produit");
		productNameCol.setMinWidth(100);
		productNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"name"));

		priceCol = new TableColumn<OrderLineTV, String>("Prix unitaire");
		priceCol.setMinWidth(100);
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

		modifiedPriceCol = new TableColumn<OrderLineTV, String>("Prix ajusté");
		modifiedPriceCol.setMinWidth(100);
		modifiedPriceCol.setCellValueFactory(new PropertyValueFactory<>(
				"modified_price"));
		
		quantityCol = new TableColumn<OrderLineTV, String>("Quantité");
		quantityCol.setMinWidth(100);
		quantityCol.setCellValueFactory(new PropertyValueFactory<>(
				"quantity"));
		
		totalCol = new TableColumn<OrderLineTV, String>("Total");
		totalCol.setMinWidth(100);
		totalCol.setCellValueFactory(new PropertyValueFactory<>(
				"total"));
	}
	
	
	public void initData(OrderTV orderTV)
	{
		orderLinesList = OrderLineManager.getByOrderId(orderTV.getId());

		orderLinesObservableList.addAll(orderLinesList);

		orderLinesTableView.setItems(orderLinesObservableList);
		
		double total = 0;
		double vendorCommission = 0;
		
		this.orderTV = orderTV;
		
		currentClient = ClientManager.getById(orderTV.getClient_id());		
		commission = CommissionManager.getById(orderTV.getCommission_id());
		
		for(OrderLineTV orderLineTV : orderLinesList)
		{
			total += orderLineTV.getTotal();
		}
		
		if(commission.getType() == 0)
		{
			double rate = (double) commission.getRate() / 100;
			System.out.println(""+ (total * rate));
			vendorCommission = total * rate;
		}
		else if(total >= commission.getMcondition())
		{
			System.out.println("fixe");
			vendorCommission = commission.getRate();
		}		
		
		orderCommentTextArea.setText(orderTV.getComment());
		vendorCommissionLabel.setText("Commission du vendeur : " + vendorCommission + " $");
		orderTotalLabel.setText("Total de la commande : " + total + " $");
		contactEmailLabel.setText("Email du contact : " + currentClient.getContact_email());
		orderNumberLabel.setText("Commande No : " + orderTV.getId());
		orderedAtLabel.setText("Commande effectuer le : " + orderTV.getOrdered_at().toString());
		contactNameLabel.setText("Nom du contact : " + currentClient.getContact_name());
		contactTelLabel.setText("Téléphone du contact : " + currentClient.getContact_tel());
		entrepriseNameLabel.setText(currentClient.getEnterprise_name());
	}
}
