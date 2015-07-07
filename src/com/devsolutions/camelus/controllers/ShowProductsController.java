package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTableView;
import com.devsolutions.camelus.managers.ProductManager;

public class ShowProductsController implements Initializable {

	@FXML
	private GridPane motherGrid;
	@FXML
	private GridPane content;
	@FXML
	private RowConstraints rowOne;
	@FXML
	private VBox gridRowOne;
	@FXML
	private Label message1;
	@FXML
	private RowConstraints rowTwo;
	@FXML
	private GridPane gridRowTwo;
	@FXML
	private TextField textFieldSearch;

	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnAddProduct;
	@FXML
	private Button btnUpdateProduct;
	@FXML
	private Button btnDeleteProduct;
	@FXML
	private Button btnPdfProduct;
	@FXML
	private Button btnShowProduct;
	@FXML
	private TableView<ProductTableView> tableViewProduct;
	private ObservableList<ProductTableView> productsObservableList;
	@FXML
	private TableColumn<ProductTableView, String> idCol;
	@FXML
	private TableColumn<ProductTableView, String> upcCol;
	@FXML
	private TableColumn<ProductTableView, String> nameCol;
	@FXML
	private TableColumn<ProductTableView, String> quantityCol;
	@FXML
	private TableColumn<ProductTableView, String> categoryCol;
	@FXML
	private TableColumn<ProductTableView, String> priceSellingcol;
	@FXML
	private SortedList<ProductTableView> sortedData;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(false);

		initTableView();
		if (productsObservableList.size() == 0) {

			noDataToShow();
		} else {
			showTableView();
		}
		FilteredList<ProductTableView> filteredData = new FilteredList<>(
				productsObservableList, p -> true);

		textFieldSearch.textProperty().addListener(
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
			Stage stage = new Stage();
			Parent root = null;
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"../views/AddProduct.fxml"));
			try {
				root = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Scene scene = new Scene(root);
			AddProductController controller = loader
					.<AddProductController> getController();
			controller.initData(this);
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();

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
							Stage stage = new Stage();
							Parent root = null;
							FXMLLoader loader = new FXMLLoader(getClass()
									.getResource("../views/UpdateProduct.fxml"));
							try {
								root = loader.load();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Scene scene = new Scene(root);
							UpdateProductController controller = loader
									.<UpdateProductController> getController();
							controller.initData(this, product, index);

							stage.setScene(scene);
							stage.initStyle(StageStyle.UNDECORATED);
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.show();

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
							Stage stage = new Stage();
							Parent root = null;
							FXMLLoader loader = new FXMLLoader(getClass()
									.getResource("../views/ShowProduct.fxml"));
							try {
								root = loader.load();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Scene scene = new Scene(root);
							ShowProductController controller = loader
									.<ShowProductController> getController();
							controller.initData(this, product, index);

							stage.setScene(scene);
							stage.initStyle(StageStyle.UNDECORATED);
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.show();

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

	private void noDataToShow() {
		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(true);
		rowOne.setPercentHeight(100);
		rowTwo.setPercentHeight(0);
	}

	public void showTableView() {
		gridRowOne.setVisible(false);
		gridRowTwo.setVisible(true);
		rowOne.setPercentHeight(0);
		rowTwo.setPercentHeight(100);
	}

	public void initTableView() {

		productsObservableList = FXCollections
				.observableArrayList(ProductManager.getAllProductTableView());

		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		upcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));

		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		categoryCol.setCellValueFactory(new PropertyValueFactory<>(
				"descriptionCategory"));

		priceSellingcol.setCellValueFactory(new PropertyValueFactory<>(
				"selling_price"));

	}

	public void selectLastRow() {
		int indexLastRow = productsObservableList.size() - 1;
		tableViewProduct.getSelectionModel().select(indexLastRow);
		tableViewProduct.getFocusModel().focus(indexLastRow);
	}

	public void selectTheModifierRow(int index) {
		tableViewProduct.getSelectionModel().select(index);
		tableViewProduct.getFocusModel().focus(index);
	}

	public void addToTableView(ProductTableView product) {
		productsObservableList.add(product);
		System.out.println("hello");
	}

	public void updateTableView(int index, ProductTableView product) {
		productsObservableList.set(index, product);
	}

	public ObservableList<ProductTableView> getProductsObservableList() {
		return productsObservableList;
	}

}
