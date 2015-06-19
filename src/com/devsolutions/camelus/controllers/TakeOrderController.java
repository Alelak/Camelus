package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.devsolutions.camelus.Listeners.AutoCompleteComboBoxListener;
import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductToOrderTV;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.managers.OrderLineManager;
import com.devsolutions.camelus.managers.OrderManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.services.Session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TakeOrderController implements Initializable {

	@FXML
	private TableView<ProductToOrderTV> orderLinesTableView;

	@FXML
	private ComboBox<Product> productComboBox;

	@FXML
	private ComboBox<Client> clientComboBox;

	@FXML
	private Label upcLabel;

	@FXML
	private Label quantityLabel;

	@FXML
	private Label unitLabel;

	@FXML
	private Label priceLabel;

	@FXML
	private Label categoryLabel;

	@FXML
	private TextField modifiedPriceField;

	@FXML
	private TextField quantityField;

	@FXML
	private TextArea commentTextArea;

	@FXML
	private Button addOrderLineBtn;

	@FXML
	private Button takeOrderBtn;

	@FXML
	private Button resetBtn;

	@FXML
	private Button removeBtn;

	@FXML
	private Button editBtn;

	private ObservableList<Product> productObservableList;
	private List<Product> products;

	private ObservableList<Client> clientObservableList;
	private List<Client> clients;

	private ObservableList<ProductToOrderTV> productToOrderObservableList;

	private TableColumn<ProductToOrderTV, String> upcCol;
	private TableColumn<ProductToOrderTV, String> productNameCol;
	private TableColumn<ProductToOrderTV, String> priceCol;
	private TableColumn<ProductToOrderTV, String> quantityCol;
	private TableColumn<ProductToOrderTV, String> modifiedPriceCol;

	private ProductToOrderTV currentProductToOrderTV;

	private Client currentClient;
	private Vendor currentVendor;
	private Stage stage;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		orderLinesTableView.getColumns().addAll(upcCol, productNameCol,
				priceCol, quantityCol, modifiedPriceCol);

		currentVendor = Session.vendor;

		products = ProductManager.getAll();
		clients = ClientManager.getByVendorId(1);

		productObservableList = FXCollections.observableArrayList();
		productObservableList.addAll(products);

		clientObservableList = FXCollections.observableArrayList();
		clientObservableList.addAll(clients);

		productComboBox.setItems(productObservableList);
		clientComboBox.setItems(clientObservableList);

		// on ajoute les autocompletes
		new AutoCompleteComboBoxListener<>(productComboBox);
		new AutoCompleteComboBoxListener<>(clientComboBox);

		addOrderLineBtn
				.setOnAction(e -> {

					if (productComboBox.getSelectionModel().getSelectedIndex() > -1) {
						if (isInteger(quantityField.getText())
								&& isDouble(modifiedPriceField.getText())) {
							currentProductToOrderTV.setQuantity(Integer
									.parseInt(quantityField.getText()));
							currentProductToOrderTV.setModified_price(Double
									.parseDouble(modifiedPriceField.getText()));

							if (!orderLinesTableView.getItems().contains(
									currentProductToOrderTV)) {
								addToTableView(currentProductToOrderTV);
								initForm();
							} else
								System.out
										.println("this item already have been added");
						} else
							System.out.println("invalide textfield");
					} else
						System.out.println("you must select a product first");
				});

		editBtn.setOnAction(e -> {
			ProductToOrderTV productToOrderTV = orderLinesTableView
					.getSelectionModel().getSelectedItem();

			productComboBox.getSelectionModel().select(
					ProductManager.getById(productToOrderTV.getId()));

			quantityField.setText("" + productToOrderTV.getQuantity());
			modifiedPriceField.setText(""
					+ productToOrderTV.getModified_price());
		});

		removeBtn.setOnAction(e -> {
			orderLinesTableView.getItems().remove(
					orderLinesTableView.getSelectionModel().getSelectedItem());
			initForm();
		});

		resetBtn.setOnAction(e -> {
			orderLinesTableView.getItems().removeAll(
					orderLinesTableView.getItems());
			initForm();
		});

		takeOrderBtn
				.setOnAction(e -> {
					if (currentClient != null
							&& !orderLinesTableView.getItems().isEmpty()) {

						Order order = new Order();
						order.setVendor_id(currentVendor.getId());
						order.setClient_id(currentClient.getId());
						order.setComment(commentTextArea.getText());

						OrderManager.add(order);

						productToOrderObservableList = orderLinesTableView
								.getItems();
						for (int i = 0; i < productToOrderObservableList.size(); i++) {
							OrderLine orderLine = new OrderLine();
							orderLine
									.setProduct_id(productToOrderObservableList
											.get(i).getId());
							orderLine.setOrder_id(order.getId());
							orderLine
									.setModified_price(productToOrderObservableList
											.get(i).getModified_price());
							orderLine.setQuantity(productToOrderObservableList
									.get(i).getQuantity());
							OrderLineManager.add(orderLine);
						}

						stage = (Stage) takeOrderBtn.getScene().getWindow();
						stage.close();
					} else
						System.out
								.println("you must choose a client and add a commande line");
				});

		orderLinesTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						editBtn.setDisable(false);
						removeBtn.setDisable(false);
					} else {
						editBtn.setDisable(true);
						removeBtn.setDisable(true);
					}
				});

	}

	private void initForm() {
		upcLabel.setText("");
		quantityLabel.setText("");
		unitLabel.setText("");
		priceLabel.setText("");
		categoryLabel.setText("");

		quantityField.setText("");
		modifiedPriceField.setText("");
	}

	private boolean isInteger(String input) {
		boolean valid = false;
		try {
			Integer.parseInt(input);
			valid = true;
		} catch (NumberFormatException e) {
			// on fait rien ici
		}

		return valid;
	}

	private boolean isDouble(String input) {
		boolean valid = false;
		try {
			Double.parseDouble(input);
			valid = true;
		} catch (NumberFormatException e) {
			// on fait rien ici
		}

		return valid;
	}

	// onAction Listner call back
	public void comboboxAction() {
		if (productComboBox.getSelectionModel().getSelectedIndex() > -1) {
			int index = productComboBox.getSelectionModel().getSelectedIndex();

			Product product = products.get(index);

			Category category = CategoryManager.getById(product
					.getCategory_id());
			Unit unit = UnitManager.getById(product.getUnit_id());

			upcLabel.setText("" + product.getUpc());
			quantityLabel.setText("" + product.getQuantity());
			unitLabel.setText("" + unit.getDescription());
			priceLabel.setText(product.getSelling_price() + " $");
			categoryLabel.setText("" + category.getDescription());

			currentProductToOrderTV = new ProductToOrderTV();

			currentProductToOrderTV.setId(product.getId());
			currentProductToOrderTV.setUpc(product.getUpc());
			currentProductToOrderTV.setName(product.getName());
			currentProductToOrderTV
					.setSelling_price(product.getSelling_price());
		}
	}

	public void initTableView() {
		productToOrderObservableList = FXCollections.observableArrayList();

		upcCol = new TableColumn<ProductToOrderTV, String>("Upc");
		upcCol.setMinWidth(100);
		upcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));

		productNameCol = new TableColumn<ProductToOrderTV, String>("Nom");
		productNameCol.setMinWidth(100);
		productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		priceCol = new TableColumn<ProductToOrderTV, String>(
				"Prix unitaire ($)");
		priceCol.setMinWidth(100);
		priceCol.setCellValueFactory(new PropertyValueFactory<>("selling_price"));

		quantityCol = new TableColumn<ProductToOrderTV, String>("Quantit�");
		quantityCol.setMinWidth(100);
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		modifiedPriceCol = new TableColumn<ProductToOrderTV, String>(
				"Prix ajust� ($)");
		modifiedPriceCol.setMinWidth(100);
		modifiedPriceCol.setCellValueFactory(new PropertyValueFactory<>(
				"modified_price"));

		orderLinesTableView.setItems(productToOrderObservableList);
	}

	public void addToTableView(ProductToOrderTV productToOrderTV) {
		orderLinesTableView.getItems().add(productToOrderTV);
	}

	public void initData(Vendor currentVendor) {
		this.currentVendor = currentVendor;
	}

	public void comboboxClientAction() {
		if (clientComboBox.getSelectionModel().getSelectedIndex() > -1) {
			int index = clientComboBox.getSelectionModel().getSelectedIndex();
			currentClient = new Client();
			currentClient = clients.get(index);
		}
	}
}
