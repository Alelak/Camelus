package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.VendorManager;

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

public class ProductViewController implements Initializable {
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
	private TableView<Product> tableViewProduct;
	private List<Product> productsList;
	private ObservableList<Product> productsObservableList;

	private TableColumn<Product, String> idCol;
	private TableColumn<Product, String> upcCol;
	private TableColumn<Product, String> nameCol;
	private TableColumn<Product, String> quantityCol;
	private TableColumn<Product, String> unitCol;
	private TableColumn<Product, String> categoryCol;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initTableView();
		tableViewProduct.getColumns().addAll(idCol, upcCol, nameCol,
				quantityCol, unitCol, categoryCol);
		btnAddProduct.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/AddProduct.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				AddProductsController controller = loader
						.<AddProductsController> getController();

				controller.initData(this);
				newStage.initModality(Modality.APPLICATION_MODAL);

				newStage.show();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	public void initTableView() {

		productsList = ProductManager.getAll();

		productsObservableList = FXCollections.observableArrayList();

		idCol = new TableColumn<Product, String>("Id");
		idCol.setMinWidth(50);
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		System.out.println("test colId ==" + idCol);
		upcCol = new TableColumn<Product, String>("UPC");
		upcCol.setMinWidth(100);
		upcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));

		nameCol = new TableColumn<Product, String>("Name");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		quantityCol = new TableColumn<Product, String>("Quantity");
		quantityCol.setMinWidth(50);
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		unitCol = new TableColumn<Product, String>("Unite");
		unitCol.setMinWidth(50);
		unitCol.setCellValueFactory(new PropertyValueFactory<>("unit_id"));

		categoryCol = new TableColumn<Product, String>("Category");
		categoryCol.setMinWidth(100);
		categoryCol.setCellValueFactory(new PropertyValueFactory<>(
				"category_id"));
		productsObservableList.addAll(productsList);

		tableViewProduct.setItems(productsObservableList);
		System.out.println("test vqleur col id == "
				+ productsObservableList.size());
		System.out.println(tableViewProduct);

	}

	public void addToTableView(Product product) {
		System.out.println("test add to table view == ");

		tableViewProduct.getItems().add(product);

	}

}
