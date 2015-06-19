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

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTableView;
import com.devsolutions.camelus.managers.ProductManager;

public class ShowProductsController implements Initializable {
	@FXML
	private Button btnSearchProduct;
	@FXML
	private Button btnAddProduct;
	@FXML
	private Button btnUpdateProduct;
	@FXML
	private Button btnDeleteProduct;
	@FXML
	private Button btnShowAllProduct;
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

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initTableView();
		tableViewProduct.getColumns().addAll(idCol, upcCol, nameCol,
				quantityCol, categoryCol, priceSellingcol);
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
		btnShowAllProduct
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

				tableViewProduct.getItems().remove(productTable);
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

		tableViewProduct.setItems(productsObservableList);

	}

	public void addToTableView(ProductTableView product) {
		tableViewProduct.getItems().add(product);
	}

	public void removeFromTableView(ProductTableView product) {
		tableViewProduct.getItems().remove(product);
	}

	public void updateTableView(int index, ProductTableView product) {
		tableViewProduct.getItems().set(index, product);
	}
}
