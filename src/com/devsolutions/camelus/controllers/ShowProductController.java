package com.devsolutions.camelus.controllers;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.utils.Choice;

public class ShowProductController implements Initializable{

	@FXML
	private Label upc;
	@FXML
	private ImageView imageProduct;
	@FXML
	private Button btnCancelProduct;
	@FXML
	private Label name;

	@FXML
	private Label quantity;
	@FXML
	private Label costPrice;
	@FXML
	private Label sellingPrice;
	@FXML
	private TextArea description;
	
	
	private Stage stage;
	
	private ByteArrayOutputStream baos = null;
	private ObservableList<Choice> listChoiceUnit;
	private ObservableList<Choice> listChoiceCategory;
	private byte[] imageInByte;
	private ShowProductsController productController;
	Product product;
	Product productToUpdate;
	int index;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		name.setText("hello");
		btnCancelProduct.setOnAction(e -> {
			stage = (Stage) btnCancelProduct.getScene().getWindow();
			stage.close();
		});
	}
	
	public void initData(ShowProductsController ProductController,Product productToUpdate,int index) {
		this.productController = ProductController;
		this.productToUpdate = productToUpdate;
		this.index = index;
		//initForm();
	}
}
