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
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.OrderLineManager;
import com.devsolutions.camelus.managers.OrderManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;

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
	private ComboBox productComboBox;

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

	private ObservableList<ProductToOrderTV> productToOrderObservableList;

	private TableColumn<ProductToOrderTV, String> upcCol;
	private TableColumn<ProductToOrderTV, String> productNameCol;
	private TableColumn<ProductToOrderTV, String> priceCol;
	private TableColumn<ProductToOrderTV, String> categoryCol;
	private TableColumn<ProductToOrderTV, String> quantityCol;
	private TableColumn<ProductToOrderTV, String> modifiedPriceCol;

	private ProductToOrderTV currentProductToOrderTV;

	private Client currentClient;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableView();
		orderLinesTableView.getColumns().addAll(upcCol, productNameCol,
				priceCol, quantityCol, modifiedPriceCol);

		products = ProductManager.getAll();
		
		productObservableList = FXCollections.observableArrayList();
		productObservableList.addAll(products);
		
		productComboBox.setItems(productObservableList);

		// on ajoute l'autocomplete
		new AutoCompleteComboBoxListener<>(productComboBox);

		addOrderLineBtn
				.setOnAction(e -> {
					int quantity = -1;
					double modifiedPrice = -1;

					if (productComboBox.getSelectionModel().getSelectedIndex() > -1) {
						if (isInteger(quantityField.getText())
								&& isDouble(modifiedPriceField.getText())) {
							quantity = Integer.parseInt(quantityField.getText());
							modifiedPrice = Double
									.parseDouble(modifiedPriceField.getText());

							currentProductToOrderTV.setQuantity(quantity);
							currentProductToOrderTV.setModified_price(quantity);

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

		takeOrderBtn.setOnAction(e -> {
			Order order = new Order();
			order.setVendor_id(1);
			order.setClient_id(currentClient.getId());
			order.setComment(commentTextArea.getText());

			OrderManager.add(order);
			System.out.println(order.getId());
			productToOrderObservableList = orderLinesTableView.getItems();
			for (int i = 0; i < productToOrderObservableList.size(); i++) {
				OrderLine orderLine = new OrderLine();
				orderLine.setProduct_id(productToOrderObservableList.get(i)
						.getId());
				orderLine.setOrder_id(order.getId());
				orderLine.setModified_price(productToOrderObservableList.get(i)
						.getModified_price());
				orderLine.setQuantity(productToOrderObservableList.get(i)
						.getQuantity());
				OrderLineManager.add(orderLine);
			}

			stage = (Stage) takeOrderBtn.getScene().getWindow();
			stage.close();
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
		productComboBox.setValue("");

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
		int number = 0;
		
		try {
			number = Integer.parseInt(input);
			valid = true;
		} catch (NumberFormatException e) {
			// on fait rien ici
		}

		return valid;
	}

	private boolean isDouble(String input) {
		boolean valid = false;
		double dnumber = 0;
		
		try {
			dnumber = Double.parseDouble(input);
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

		quantityCol = new TableColumn<ProductToOrderTV, String>("Quantité");
		quantityCol.setMinWidth(100);
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		modifiedPriceCol = new TableColumn<ProductToOrderTV, String>(
				"Prix ajusté ($)");
		modifiedPriceCol.setMinWidth(100);
		modifiedPriceCol.setCellValueFactory(new PropertyValueFactory<>(
				"modified_price"));

		orderLinesTableView.setItems(productToOrderObservableList);
	}

	public void addToTableView(ProductToOrderTV productToOrderTV) {
		orderLinesTableView.getItems().add(productToOrderTV);
	}

	public void initData(Client currentClient) {
		this.currentClient = currentClient;
	}
}
