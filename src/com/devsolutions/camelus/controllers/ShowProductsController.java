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
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTableView;

import com.devsolutions.camelus.managers.ProductManager;

public class ShowProductsController implements Initializable {
	@FXML
	private Button btnSearchProduct;
	@FXML
	private TextField txtFiledSearch;
	@FXML
	private Button btnAddProduct;
	@FXML
	private Button btnUpdateProduct;
	@FXML
	private Button btnDeleteProduct;
	@FXML
	private Button btnShowProduct;
	@FXML
	private TableView<ProductTableView> tableViewProduct;
	private List<ProductTableView> productsList;
	private ObservableList<ProductTableView> productsObservableList;

	private TableColumn<ProductTableView, String> idCol;
	private TableColumn<ProductTableView, String> upcCol;
	private TableColumn<ProductTableView, String> nameCol;
	private TableColumn<ProductTableView, String> quantityCol;
	private TableColumn<ProductTableView, String> categoryCol;
	private TableColumn<ProductTableView, String> priceSellingcol;
	private SortedList<ProductTableView> sortedData;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initTableView();
		tableViewProduct.getColumns().addAll(idCol, upcCol, nameCol,
				quantityCol, categoryCol, priceSellingcol);

		FilteredList<ProductTableView> filteredData = new FilteredList<>(
				productsObservableList, p -> true);

		txtFiledSearch.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(ProductTableView -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}

						String lowerCaseFilter = newValue.toLowerCase();
						String id = ProductTableView.getId() + "";

						if (ProductTableView.getUpc().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (ProductTableView.getName().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (ProductTableView.getDescriptionCategory()
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
				tableViewProduct.comparatorProperty());

		tableViewProduct.setItems(sortedData);

		btnAddProduct.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/AddProduct.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				AddProductController controller = loader
						.<AddProductController> getController();

				controller.initData(this);
				newStage.initModality(Modality.APPLICATION_MODAL);

				newStage.show();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		btnUpdateProduct
				.setOnAction(e -> {
					int index = tableViewProduct.getSelectionModel()
							.getSelectedIndex();
					if (index > -1) {
						ProductTableView productTableView = tableViewProduct
								.getSelectionModel().getSelectedItem();

						Product product = ProductManager
								.getById(productTableView.getId());

						if (product != null) {
							try {
								FXMLLoader loader = new FXMLLoader(getClass()
										.getResource(
												"../views/UpdateProduct.fxml"));

								Stage newStage = new Stage();
								Scene scene;

								scene = new Scene(loader.load());

								newStage.setScene(scene);

								UpdateProductController controller = loader
										.<UpdateProductController> getController();
								controller.initData(this, product, index);

								newStage.initModality(Modality.APPLICATION_MODAL);

								newStage.show();

							} catch (Exception e1) {
								e1.printStackTrace();
							}

						}
					}
				});
		btnShowProduct
				.setOnAction(e -> {
					int index = tableViewProduct.getSelectionModel()
							.getSelectedIndex();
					if (index > -1) {
						ProductTableView productTableView = tableViewProduct
								.getSelectionModel().getSelectedItem();

						Product product = ProductManager
								.getById(productTableView.getId());

						if (product != null) {
							try {
								FXMLLoader loader = new FXMLLoader(getClass()
										.getResource(
												"../views/ShowProduct.fxml"));

								Stage newStage = new Stage();
								Scene scene;

								scene = new Scene(loader.load());

								newStage.setScene(scene);

								ShowProductController controller = loader
										.<ShowProductController> getController();
								controller.initData(this, product, index);

								newStage.initModality(Modality.APPLICATION_MODAL);

								newStage.show();

							} catch (Exception e1) {
								e1.printStackTrace();
							}

						}
					}
				});
		btnDeleteProduct.setOnAction(e -> {
			ProductTableView productTable = tableViewProduct
					.getSelectionModel().getSelectedItem();
			if (productTable != null) {
				ProductManager.delete(productTable.getId());

				productsObservableList.remove(productTable);
			}

		});
		tableViewProduct.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						btnDeleteProduct.setDisable(false);
						btnShowProduct.setDisable(false);
						btnUpdateProduct.setDisable(false);
					} else {
						btnDeleteProduct.setDisable(true);
						btnShowProduct.setDisable(true);
						btnUpdateProduct.setDisable(true);
					}
				});
	}

	public void initTableView() {

		productsList = ProductManager.getAllProductTableView();

		productsObservableList = FXCollections.observableArrayList();

		idCol = new TableColumn<ProductTableView, String>("Id");
		idCol.setMinWidth(50);
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		upcCol = new TableColumn<ProductTableView, String>("UPC");
		upcCol.setMinWidth(100);
		upcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));

		nameCol = new TableColumn<ProductTableView, String>("Name");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		quantityCol = new TableColumn<ProductTableView, String>("Quantity");
		quantityCol.setMinWidth(50);
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		categoryCol = new TableColumn<ProductTableView, String>("Category");
		categoryCol.setMinWidth(100);
		categoryCol.setCellValueFactory(new PropertyValueFactory<>(
				"descriptionCategory"));
		priceSellingcol = new TableColumn<ProductTableView, String>("Price ($)");
		priceSellingcol.setMinWidth(50);
		priceSellingcol.setCellValueFactory(new PropertyValueFactory<>(
				"selling_price"));

		productsObservableList.addAll(productsList);

	}

	public void addToTableView(ProductTableView product) {
		productsObservableList.add(product);
	}

	public void updateTableView(int index, ProductTableView product) {
		productsObservableList.set(index, product);
	}
}
