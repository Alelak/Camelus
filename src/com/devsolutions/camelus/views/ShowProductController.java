package com.devsolutions.camelus.views;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.utils.FXUtils;

public class ShowProductController implements Initializable {
	@FXML
	private GridPane titleBar;
	@FXML
	private Label upc;
	@FXML
	private ImageView imageProduct;
	@FXML
	private Button btnCancelProduct;
	@FXML
	private Label name;
	@FXML
	private Label unit;
	@FXML
	private Label category;
	@FXML
	private Label quantity;
	@FXML
	private Label costPrice;
	@FXML
	private Label sellingPrice;
	@FXML
	private TextArea description;
	@FXML
	private Label lblClose;
	private Stage stage;

	private byte[] imageInByte;
	Product product;
	Product productToUpdate;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);

		btnCancelProduct.setOnAction(e -> {
			stage = (Stage) btnCancelProduct.getScene().getWindow();
			stage.close();
		});
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}

	public void initData(ShowProductsController ProductController,
			Product productToUpdate, int index) {

		this.productToUpdate = productToUpdate;

		initForm();
	}

	private void initForm() {
		upc.setText(productToUpdate.getUpc());
		name.setText(productToUpdate.getName());
		quantity.setText("" + productToUpdate.getQuantity());

		costPrice.setText("" + productToUpdate.getCost_price());
		sellingPrice.setText("" + productToUpdate.getSelling_price());
		description.setText(productToUpdate.getDescription());
		unit.setText(UnitManager.getById(productToUpdate.getUnit_id())
				.getDescription());
		category.setText(CategoryManager.getById(
				productToUpdate.getCategory_id()).getDescription());
		imageInByte = productToUpdate.getImg();
		if (imageInByte != null)
			Showimage();
		else {
			imageProduct.setImage(new Image(getClass().getResourceAsStream(
					"../../../../images/nopicture.jpg")));
		}

	}

	private void Showimage() {
		ByteArrayInputStream is = new ByteArrayInputStream(imageInByte);
		imageProduct.setImage(new Image(is));

	}

}
