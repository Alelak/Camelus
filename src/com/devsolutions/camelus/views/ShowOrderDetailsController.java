package com.devsolutions.camelus.views;

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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.OrderLineManager;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;

public class ShowOrderDetailsController implements Initializable {
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
	private TableColumn<OrderLineTV, String> productUpcCol;
	@FXML
	private TableColumn<OrderLineTV, String> productNameCol;
	@FXML
	private TableColumn<OrderLineTV, String> quantityCol;
	@FXML
	private TableColumn<OrderLineTV, String> priceCol;
	@FXML
	private TableColumn<OrderLineTV, String> modifiedPriceCol;
	@FXML
	private TableColumn<OrderLineTV, String> totalCol;

	@FXML
	private Button doneBtn;
	@FXML
	private Label lblClose;
	@FXML
	private GridPane titleBar;
	private List<OrderLineTV> orderLinesList;
	private ObservableList<OrderLineTV> orderLinesObservableList;

	private Client currentClient;
	private Commission commission;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		initTableView();

		doneBtn.setOnAction(e -> {
			stage = (Stage) doneBtn.getScene().getWindow();
			stage.close();
		});
	}

	private void initTableView() {
		orderLinesObservableList = FXCollections.observableArrayList();

		productUpcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));
		productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
		modifiedPriceCol.setCellValueFactory(new PropertyValueFactory<>(
				"modified_price_txt"));
		totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
	}

	public void initData(OrderTV orderTV) {
		orderLinesList = OrderLineManager.getByOrderId(orderTV.getId());
		orderLinesObservableList.addAll(orderLinesList);

		orderLinesTableView.setItems(orderLinesObservableList);

		double total = 0;
		double vendorCommission = 0;

		currentClient = ClientManager.getById(orderTV.getClient_id());
		commission = CommissionManager.getById(orderTV.getCommission_id());

		for (OrderLineTV orderLineTV : orderLinesList) {
			total += orderLineTV.getTotal();
		}

		if (total >= commission.getMcondition()) {
			if (commission.getType() == 0) {
				double rate = (double) commission.getRate() / 100;
				vendorCommission = total * rate;
			} else {
				vendorCommission = commission.getRate();
			}
		} else
			vendorCommission = 0;
		orderCommentTextArea.setText(orderTV.getComment());
		vendorCommissionLabel.setText("Commission du vendeur : "
				+ vendorCommission + " $");
		orderTotalLabel.setText("Total de la commande : " + total + " $");
		contactEmailLabel.setText(currentClient.getContact_email());
		orderNumberLabel.setText("" + orderTV.getId());

		orderedAtLabel.setText(StringUtils.formatDate(orderTV.getOrdered_at()));
		contactNameLabel.setText(currentClient.getContact_name());
		contactTelLabel.setText(currentClient.getContact_tel());
		entrepriseNameLabel.setText(currentClient.getEnterprise_name());
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}
}
