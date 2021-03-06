package com.devsolutions.camelus.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.devsolutions.camelus.Listeners.AutoCompleteComboBoxListener;
import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductToOrderTV;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.OrderLineManager;
import com.devsolutions.camelus.managers.OrderManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;

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
	private Label lblClose;
	@FXML
	private Label quantityLabel;
	@FXML
	private Label unitLabel;
	@FXML
	private Label costPriceLabel;
	@FXML
	private Label sellingPriceLabel;
	@FXML
	private Label categoryLabel;
	@FXML
	private Label orderTotalTxt;

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
	@FXML
	private GridPane titleBar;
	@FXML
	private TableColumn<ProductToOrderTV, String> upcCol;
	@FXML
	private TableColumn<ProductToOrderTV, String> productNameCol;
	@FXML
	private TableColumn<ProductToOrderTV, String> priceCol;
	@FXML
	private TableColumn<ProductToOrderTV, String> quantityCol;
	@FXML
	private TableColumn<ProductToOrderTV, String> modifiedPriceCol;

	@FXML
	private ImageView productImage;

	private ObservableList<Product> productObservableList;
	private List<Product> products;

	private ObservableList<Client> clientObservableList;
	private List<Client> clients;

	private ObservableList<ProductToOrderTV> productToOrderObservableList;
	private ObservableList<Vendor> vendorObservableList;

	private ProductToOrderTV currentProductToOrderTV;

	private Client currentClient;
	private Product currentProduct;
	private Stage stage;

	private ShowOrdersController showOrdersController;
	private SimpleBooleanProperty productfound;
	private SimpleBooleanProperty clientfound;
	private List<Category> categories;
	private List<Unit> unites;
	private ProductToOrderTV selectedProductToModifie;
	private Image noImage;
	private String quantity;
	private String modifiedPrice;
	private double minModifiedPrice;
	private double total;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		initTableView();

		productfound = new SimpleBooleanProperty();
		clientfound = new SimpleBooleanProperty();
		categories = CategoryManager.getAll();
		unites = UnitManager.getAll();
		noImage = productImage.getImage();

		products = ProductManager.getAll();
		if (Session.admin != null) {
			clients = ClientManager.getAll();
		} else {
			clients = ClientManager.getByVendorId(Session.vendor.getId());
		}

		productObservableList = FXCollections.observableArrayList();
		for (Product p : products) {
			if (p.getSelling_price() > 0) {
				productObservableList.add(p);
			}
		}
		clientObservableList = FXCollections.observableArrayList();
		clientObservableList.addAll(clients);

		if (Session.vendor != null) {
			vendorObservableList = FXCollections.observableArrayList();
			vendorObservableList.add(Session.vendor);
		} else
			vendorObservableList = FXCollections
					.observableArrayList(VendorManager.getAll());

		productComboBox.setItems(productObservableList);
		clientComboBox.setItems(clientObservableList);
		new AutoCompleteComboBoxListener<Product>(productComboBox);
		new AutoCompleteComboBoxListener<Client>(clientComboBox);
		addOrderLineBtn
				.setOnAction(e -> {
					int index = 0;
					if (addOrderLineBtn.getText().equals("Modifier")) {
						index = orderLinesTableView.getSelectionModel()
								.getSelectedIndex();
					}
					String invalidFields = "";
					boolean validfields = true;
					quantity = quantityField.getText().trim();
					modifiedPrice = modifiedPriceField.getText().trim();
					minModifiedPrice = currentProduct.getCost_price()
							+ (currentProduct.getCost_price() * 10) / 100;

					if (!StringUtils.isInteger(quantity)) {
						invalidFields += "Veuillez saisir une quantité valide. \n";
						validfields = false;
					} else if (Integer.parseInt(quantity) <= 0) {
						invalidFields += "Veuillez saisir une quantité valide. \n";
						validfields = false;
					} else if (Integer.parseInt(quantity) > currentProduct
							.getQuantity()) {
						invalidFields += "La quantite disponible ne peut satisfaire votre demande! \n";
						validfields = false;
					}

					if (!modifiedPrice.isEmpty()) {
						if (!StringUtils.validDecimal(modifiedPrice)) {
							invalidFields += "Veuillez saisir un prix valide \n";
							validfields = false;
						} else if (Double.parseDouble(modifiedPriceField
								.getText()) < minModifiedPrice) {
							invalidFields += "Le prix ajuste ne peut etre plus petit que : "
									+ minModifiedPrice + " $.\n";
							validfields = false;
						}
					}

					if (addOrderLineBtn.getText().equals("Ajouter"))
						for (ProductToOrderTV productToOrderTV : orderLinesTableView
								.getItems()) {
							if (productToOrderTV.getId() == currentProduct
									.getId()) {
								invalidFields += "Ce produit existe deja dans la liste. Veuillez le modifier ou le supprimer  \n";
								validfields = false;
							}
						}

					if (addOrderLineBtn.getText().equals("Modifier")) {
						for (ProductToOrderTV productToOrderTV : orderLinesTableView
								.getItems()) {

							if (productToOrderTV.getId() == currentProduct
									.getId()) {
								if (currentProduct.getId() != selectedProductToModifie
										.getId()) {
									invalidFields += "Ce produit existe deja dans la liste. Veuillez le modifier ou le supprimmer  \n";
									validfields = false;

								}
							}
						}
					}

					if (validfields) {
						if (addOrderLineBtn.getText().equals("Modifier")) {
							if (selectedProductToModifie.getModified_price() == 0) {
								substractFromOrderTotal(selectedProductToModifie
										.getSelling_price()
										* selectedProductToModifie
												.getQuantity());
							} else {
								substractFromOrderTotal(selectedProductToModifie
										.getModified_price()
										* selectedProductToModifie
												.getQuantity());
							}
						}
						if (!clientComboBox.isDisabled()) {
							clientComboBox.setDisable(true);
						}

						int mquantity = Integer.parseInt(quantity);
						currentProductToOrderTV = new ProductToOrderTV();
						currentProductToOrderTV.setQuantity(mquantity);
						currentProductToOrderTV.setId(currentProduct.getId());
						currentProductToOrderTV.setUpc(currentProduct.getUpc());
						currentProductToOrderTV.setName(currentProduct
								.getName());
						currentProductToOrderTV.setSelling_price(currentProduct
								.getSelling_price());

						if (!modifiedPrice.isEmpty()) {
							double mprice = Double.parseDouble(modifiedPrice);
							currentProductToOrderTV.setModified_price(mprice);
							addToOrderTotal(mprice * mquantity);

						} else {
							addToOrderTotal(currentProduct.getSelling_price()
									* mquantity);
							currentProductToOrderTV.setModified_price(0);
						}
						if (addOrderLineBtn.getText().equals("Modifier")) {
							updateTableView(index, currentProductToOrderTV);
						} else {
							addToTableView(currentProductToOrderTV);
						}
						initForm();
						currentProduct = new Product();
						productComboBox.getSelectionModel().clearSelection();
						takeOrderBtn.setDisable(false);

					} else {
						try {
							stage = (Stage) addOrderLineBtn.getScene()
									.getWindow();
							new CustomInfoBox(stage, BoxType.INFORMATION,
									invalidFields, "Ok");
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				});

		editBtn.setOnAction(e -> {
			selectedProductToModifie = orderLinesTableView.getSelectionModel()
					.getSelectedItem();
			editBtn.setDisable(true);
			for (Product product : productComboBox.getItems()) {
				if (product.getId() == selectedProductToModifie.getId()) {
					productComboBox.getSelectionModel().select(product);
				}
			}

			quantityField.setText("" + selectedProductToModifie.getQuantity());
			if (selectedProductToModifie.getModified_price() > 0) {
				modifiedPriceField.setText(""
						+ selectedProductToModifie.getModified_price());
			}
			takeOrderBtn.setDisable(true);
			addOrderLineBtn.setText("Modifier");
			if (orderLinesTableView.getItems().size() == 1) {
				clientComboBox.setDisable(false);
			}

		});

		removeBtn.setOnAction(e -> {
			ProductToOrderTV productToOrderTV = orderLinesTableView
					.getSelectionModel().getSelectedItem();
			orderLinesTableView.getItems().remove(productToOrderTV);
			if (productToOrderTV.getModified_price() == 0) {
				substractFromOrderTotal(productToOrderTV.getSelling_price()
						* productToOrderTV.getQuantity());
			} else {
				substractFromOrderTotal(productToOrderTV.getModified_price()
						* productToOrderTV.getQuantity());
			}
			initForm();
			if (addOrderLineBtn.getText().equals("Modifier")) {
				addOrderLineBtn.setText("Ajouter");
			}
			if (orderLinesTableView.getItems().size() == 0) {
				takeOrderBtn.setDisable(true);
				clientComboBox.setDisable(false);
			}
		});

		resetBtn.setOnAction(e -> {
			orderTotalTxt.setText("0.0$");
			total = 0;
			orderLinesTableView.getItems().removeAll(
					orderLinesTableView.getItems());
			productComboBox.getSelectionModel().clearSelection();
			initForm();
			takeOrderBtn.setDisable(true);
			clientComboBox.setDisable(false);
		});

		takeOrderBtn
				.setOnAction(e -> {
					stage = (Stage) takeOrderBtn.getScene().getWindow();
					String comment = commentTextArea.getText().trim();
					if (comment.length() > 255) {
						try {
							new CustomInfoBox(
									stage,
									BoxType.WARNING,
									"Le commentaire ne doit pas depasse 255 caractères ",
									"Ok");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						Vendor vendor = VendorManager.getById(currentClient
								.getAssociated_vendor());
						OrderTV orderTV = new OrderTV();
						Commission commission = CommissionManager
								.getById(vendor.getCommission_id());
						Order order = new Order();
						order.setCommission(0.0);
						if (total >= commission.getMcondition()) {
							if (commission.getType() == 0) {
								order.setCommission(total
										* commission.getRate() / 100);
							} else {
								order.setCommission(commission.getRate());
							}
						}
						order.setTotal(total);
						order.setVendor_id(currentClient.getAssociated_vendor());
						order.setClient_id(currentClient.getId());
						order.setComment(comment);

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

						orderTV.setCommission_id(vendor.getCommission_id());
						orderTV.setEnterprise_name(currentClient
								.getEnterprise_name());
						orderTV.setFname(vendor.getFname());
						orderTV.setLname(vendor.getLname());
						orderTV.setOrdered_at(new Date());

						for (ProductToOrderTV productToOrderTV : orderLinesTableView
								.getItems()) {
							ProductManager.decrementQuantity(
									productToOrderTV.getQuantity(),
									productToOrderTV.getId());
						}

						showOrdersController.addToTableView(orderTV);
						showOrdersController.showTableView();

						stage.close();
					}
				});

		orderLinesTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						if (!addOrderLineBtn.getText().equals("Modifier")) {
							editBtn.setDisable(false);
							removeBtn.setDisable(false);
						}
					} else {
						editBtn.setDisable(true);
						removeBtn.setDisable(true);
						addOrderLineBtn.setText("Ajouter");
					}
				});
		productfound.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue.booleanValue() == true
						&& clientfound.get() == true) {
					addOrderLineBtn.setDisable(false);
				} else {
					addOrderLineBtn.setDisable(true);
				}

			}

		});
		clientfound.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue.booleanValue() == true
						&& productfound.get() == true) {

					addOrderLineBtn.setDisable(false);
				} else {
					addOrderLineBtn.setDisable(true);
				}

			}

		});
		productComboBox.setOnAction(e -> {

			if (productComboBox.getSelectionModel().getSelectedIndex() > -1) {
				int index = productComboBox.getSelectionModel()
						.getSelectedIndex();
				currentProduct = productComboBox.getItems().get(index);
				quantityField.setEditable(true);
				modifiedPriceField.setEditable(true);
				ByteArrayInputStream is = null;
				if (currentProduct.getImg() != null) {
					is = new ByteArrayInputStream(currentProduct.getImg());
					productImage.setImage(new Image(is));
				} else
					productImage.setImage(noImage);
				for (Unit unit : unites) {
					if (unit.getId() == currentProduct.getUnit_id()) {
						unitLabel.setText("" + unit.getDescription());
					}
				}
				for (Category category : categories) {
					if (category.getId() == currentProduct.getCategory_id()) {
						categoryLabel.setText("" + category.getDescription());
					}
				}
				upcLabel.setText("" + currentProduct.getUpc());
				quantityLabel.setText("" + currentProduct.getQuantity());
				costPriceLabel.setText(currentProduct.getCost_price() + " $");
				sellingPriceLabel.setText(currentProduct.getSelling_price()
						+ " $");
				productfound.set(true);
			} else {
				productfound.set(false);
			}

		});
		clientComboBox.setOnAction(e -> {
			if (clientComboBox.getSelectionModel().getSelectedIndex() > -1) {
				int index = clientComboBox.getSelectionModel()
						.getSelectedIndex();
				currentClient = clientComboBox.getItems().get(index);

				clientfound.set(true);
			} else {

				clientfound.set(false);
			}
		});
	}

	private void initForm() {
		upcLabel.setText("");
		quantityLabel.setText("");
		unitLabel.setText("");
		costPriceLabel.setText("");
		sellingPriceLabel.setText("");
		categoryLabel.setText("");
		productImage.setImage(noImage);
		quantityField.setText("");
		modifiedPriceField.setText("");
	}

	private void initTableView() {
		productToOrderObservableList = FXCollections.observableArrayList();

		upcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));
		productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		modifiedPriceCol.setCellValueFactory(new PropertyValueFactory<>(
				"modified_price"));

		orderLinesTableView.setItems(productToOrderObservableList);

		orderLinesTableView.setPlaceholder(new Label(
				"Aucun produit dans la liste"));
	}

	public void addToTableView(ProductToOrderTV productToOrderTV) {
		orderLinesTableView.getItems().add(productToOrderTV);
	}

	public void updateTableView(int index, ProductToOrderTV productToOrderTV) {
		orderLinesTableView.getItems().set(index, productToOrderTV);
	}

	public void initData(ShowOrdersController showOrdersController) {
		this.showOrdersController = showOrdersController;
	}

	@FXML
	private void CloseWindow() {
		stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}

	private void addToOrderTotal(double value) {
		total += value;
		total = Math.floor(total * 100) / 100;
		orderTotalTxt.setText(total + "$");
	}

	private void substractFromOrderTotal(double value) {
		total -= value;
		total = Math.floor(total * 100) / 100;
		orderTotalTxt.setText(total + "$");
	}
}
