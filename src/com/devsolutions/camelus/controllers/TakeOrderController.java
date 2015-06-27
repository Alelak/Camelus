package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.devsolutions.camelus.Listeners.AutoCompleteComboBoxListener;
import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.entities.OrderTV;
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
import com.devsolutions.camelus.utils.CustomInfoBox;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private Product currentProduct;
	private Vendor currentVendor;
	private Stage stage;
	
	private ShowOrdersController showOrdersController;

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

					String invalidFields = "";
					boolean validfields = true;
					
					if (isInteger(quantityField.getText()) && quantityField.getText().isEmpty())
					{
						invalidFields += " - La quantité est un champ obligatoire. \n";
						validfields = false;
					}
					
					if (isInteger(quantityField.getText()) && Integer.parseInt(quantityField.getText()) <= 0)
					{
						invalidFields += " - La quantité doit être un nombre strictement positif. \n";
						validfields = false;
					}	
					
					if (!isInteger(quantityField.getText()))
					{
						invalidFields += " - Veillez  saisir un nombre strictement positif pour le champ (Quantité). \n";
						validfields = false;
					}	
					
					if(isInteger(quantityField.getText()) && Integer.parseInt(quantityField.getText()) > currentProduct.getQuantity() ){
						invalidFields += " - La quantité disponible ne peut satisfaire votre demande! \n";
						validfields = false;
					}
					
					if (isDouble(modifiedPriceField.getText()) && Double.parseDouble(modifiedPriceField.getText()) <= 0 && !modifiedPriceField.getText().isEmpty())
					{
						invalidFields += " - Le prix ajusté doit être un nombre décimal strictement positif. \n";
						validfields = false;
					}
					
					if (!isDouble(modifiedPriceField.getText()) && !modifiedPriceField.getText().isEmpty())
					{
						invalidFields += " - Veillez  saisir un nombre décimal strictement positif pour le champ (Prix ajusté). \n";
						validfields = false;
					}
					if (orderLinesTableView.getItems().contains(currentProductToOrderTV) && !addOrderLineBtn.getText().equals("Modifier"))
					{
						invalidFields += " - Ce produit existe déjà dans la liste. Veillez le modifier ou le supprimmer \n";
						validfields = false;
					}

					if (validfields) {
						currentProductToOrderTV.setQuantity(Integer.parseInt(quantityField.getText()));
						
						if(!modifiedPriceField.getText().isEmpty())
							currentProductToOrderTV.setModified_price(Double.parseDouble(modifiedPriceField.getText()));
						else
							currentProductToOrderTV.setModified_price(0);
						
						int index = orderLinesTableView.getSelectionModel().getSelectedIndex();
						
						if(addOrderLineBtn.getText().equals("Modifier"))
						{
							addOrderLineBtn.setText("Ajouter");
							updateTableView(index, currentProductToOrderTV);
							initForm();
						}
						else
						{
							addToTableView(currentProductToOrderTV);
							initForm();
						}
					} else{
						try {
							CustomInfoBox customDialogBox = new CustomInfoBox(stage,
									invalidFields, "Ok", "#ff0000");
							customDialogBox.btn
									.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											stage = (Stage) customDialogBox.btn
													.getScene().getWindow();
											stage.close();
										}
									});
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				});

		editBtn.setOnAction(e -> {
			ProductToOrderTV productToOrderTV = orderLinesTableView
					.getSelectionModel().getSelectedItem();

			productComboBox.getSelectionModel().select(
					ProductManager.getById(productToOrderTV.getId()));

			quantityField.setText("" + productToOrderTV.getQuantity());
			modifiedPriceField.setText(""
					+ productToOrderTV.getModified_price());
			addOrderLineBtn.setText("Modifier");
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
					
					String invalidFields = "";
					boolean validfields = true;
					
					if (orderLinesTableView.getItems().isEmpty())
					{
						invalidFields += " - Il faut ajouter au moins un produit à la liste de commandes. \n";
						validfields = false;
					}

					if (validfields) {
						OrderTV orderTV = new OrderTV();
						
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
						
						orderTV.setClient_id(order.getClient_id());
						orderTV.setId(order.getId());
						orderTV.setComment(order.getComment());
						orderTV.setCommission_id(currentVendor.getCommission_id());
						orderTV.setEnterprise_name(currentClient.getEnterprise_name());
						orderTV.setFname(currentVendor.getFname());
						orderTV.setLname(currentVendor.getLname());
						orderTV.setOrdered_at(new Date());
						
						for(ProductToOrderTV productToOrderTV : orderLinesTableView.getItems())
						{
							ProductManager.decrementQuantity(productToOrderTV.getQuantity(), productToOrderTV.getId());
						}
						
						showOrdersController.addToTableView(orderTV);
						
						stage = (Stage) takeOrderBtn.getScene().getWindow();
						stage.close();
					} else{
						try {
							CustomInfoBox customDialogBox = new CustomInfoBox(stage,
									invalidFields, "Ok", "#ff0000");
							customDialogBox.btn
									.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											stage = (Stage) customDialogBox.btn
													.getScene().getWindow();
											stage.close();
										}
									});
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}	
				});

		orderLinesTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						editBtn.setDisable(false);
						removeBtn.setDisable(false);
					} else {
						editBtn.setDisable(true);
						removeBtn.setDisable(true);
						addOrderLineBtn.setText("Ajouter");
					}
				});

		productComboBox.getEditor().textProperty()
				.addListener(new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (checkProductClientSelection()) {
							takeOrderBtn.setDisable(false);
						} else {
							takeOrderBtn.setDisable(true);
						}
					}
				});

		clientComboBox.getEditor().textProperty()
				.addListener(new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (checkProductClientSelection()) {
							takeOrderBtn.setDisable(false);

						} else {
							takeOrderBtn.setDisable(true);
						}
					}
				});
	}
	
	private boolean checkProductClientSelection() {
		boolean bothFound = false;
		
		boolean productFound = false;
		boolean clientFound = false;
		
		for(Product product : productComboBox.getItems()){
			if(product.getName().equals(productComboBox.getEditor().getText()))
			{
				productFound = true;
				currentProduct = product;
				productComboBox.getSelectionModel().select(product);
				addOrderLineBtn.setDisable(false);
			}
		}
		
		for(Client client : clientComboBox.getItems()){
			if(client.getEnterprise_name().equals(clientComboBox.getEditor().getText()))
			{
				clientFound = true;
				currentClient = client;
				clientComboBox.getSelectionModel().select(client);
			}
		}
		
		if(clientFound && productFound)
		{
			bothFound = true;
			
		}else if(!productFound)
		{
			initForm();
			addOrderLineBtn.setDisable(true);
		}
		
		return bothFound;
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
			currentProduct = new Product();
			currentProduct = products.get(index);

			Category category = CategoryManager.getById(currentProduct
					.getCategory_id());

			Unit unit = UnitManager.getById(currentProduct.getUnit_id());

			upcLabel.setText("" + currentProduct.getUpc());
			quantityLabel.setText("" + currentProduct.getQuantity());
			unitLabel.setText("" + unit.getDescription());
			priceLabel.setText(currentProduct.getSelling_price() + " $");
			categoryLabel.setText("" + category.getDescription());

			currentProductToOrderTV = new ProductToOrderTV();

			currentProductToOrderTV.setId(currentProduct.getId());
			currentProductToOrderTV.setUpc(currentProduct.getUpc());
			currentProductToOrderTV.setName(currentProduct.getName());
			currentProductToOrderTV.setSelling_price(currentProduct
					.getSelling_price());
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
	
	public void updateTableView(int index, ProductToOrderTV productToOrderTV) {
		orderLinesTableView.getItems().set(index, productToOrderTV);
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
	
	public void initData(ShowOrdersController showOrdersController) {
		this.showOrdersController = showOrdersController;
	}
}
