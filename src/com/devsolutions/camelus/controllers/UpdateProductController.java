package com.devsolutions.camelus.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTableView;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.utils.Choice;

public class UpdateProductController implements Initializable {
	@FXML
	private TextField upc;
	@FXML
	private ImageView imageProduct;
	@FXML
	private Button btnCancelProduct;
	@FXML
	private Button btnUpdateProduct;
	@FXML
	private Button btnAddUnit;
	@FXML
	private Button btnAddImg;
	@FXML
	private TextField name;
	@FXML
	private TextField quantity;
	@FXML
	private TextField costPrice;
	@FXML
	private TextField sellingPrice;
	@FXML
	private TextArea description;
	@FXML
	private ChoiceBox<Choice> category;
	@FXML
	private TextField imgUrl;
	@FXML
	private ChoiceBox<Choice> unit;
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

		btnAddImg.setOnAction(e -> {
			System.out.println(category.getValue().toString());
			addPicture();
			Showimage();
		});
		btnUpdateProduct.setOnAction(e -> {
			if ((unit.getSelectionModel().getSelectedIndex() == 0)
					|| (category.getSelectionModel().getSelectedIndex() == 0))
				System.out.println("il faut choisir une unit ou une category");

			if (!upc.getText().isEmpty() && !name.getText().isEmpty()
					&& !quantity.getText().isEmpty() && imageInByte != null
					&& !description.getText().isEmpty()
					&& !sellingPrice.getText().isEmpty()
					&& !costPrice.getText().isEmpty()) {
				ProductManager.update(updateProduct());
				ProductTableView productTable = new ProductTableView();
				productTable.setId(product.getId());
				System.out.println("valeur produit`==" + product.getId());
				productTable.setName(product.getName());
				productTable.setQuantity(product.getQuantity());
				productTable.setUpc(product.getUpc());
				productTable.setSelling_price(product.getSelling_price());
				productTable.setDescriptionCategory(category.getValue()
						.toString());
				productController.updateTableView(index, productTable);
				stage = (Stage) btnUpdateProduct.getScene().getWindow();
				stage.close();
			} else
				System.out.println("tous les chams doivent etre remplis");

		});
		btnCancelProduct.setOnAction(e -> {
			stage = (Stage) btnCancelProduct.getScene().getWindow();
			stage.close();
		});

	}

	public Product updateProduct()

	{

		product = new Product();
		product.setId(productToUpdate.getId());
		product.setUpc(upc.getText());
		product.setName(name.getText());
		product.setQuantity(Integer.parseInt(quantity.getText()));
		product.setUnit_id(unit.getSelectionModel().getSelectedItem().getId());
		product.setCategory_id(category.getSelectionModel().getSelectedItem()
				.getId());
		product.setImg(imageInByte);
		product.setDescription(description.getText());
		product.setCost_price(Double.parseDouble(costPrice.getText()));
		product.setSelling_price(Double.parseDouble(sellingPrice.getText()));

		return product;
	}

	public void listChoiceBoxUnit(int index) {
		List<Unit> unitList = UnitManager.getAll();
		listChoiceUnit = FXCollections.observableArrayList();
		listChoiceUnit.add(new Choice(0, "No Selection"));
		for (Unit unit : unitList) {
			listChoiceUnit.add(new Choice(unit.getId(), unit.getDescription()));
		}
		unit.setItems(listChoiceUnit);
		unit.getSelectionModel().select(index);
	}

	public void listChoiceBoxCategory(int index) {
		List<Category> CategoryList = CategoryManager.getAll();
		listChoiceCategory = FXCollections.observableArrayList();
		listChoiceCategory.add(new Choice(0, "No Selection"));
		for (Category category : CategoryList) {
			listChoiceCategory.add(new Choice(category.getId(), category
					.getDescription()));
		}

		category.setItems(listChoiceCategory);
		category.getSelectionModel().select(index);

	}

	public void addPicture() {

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"All Images", "*.*");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {

			try {
				BufferedImage originalImage = ImageIO.read(file);
				baos = new ByteArrayOutputStream();
				ImageIO.write(originalImage, "jpg", baos);
				baos.flush();
				imageInByte = baos.toByteArray();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void initForm() {
		upc.setText("" + productToUpdate.getUpc());
		name.setText(productToUpdate.getName());
		quantity.setText("" + productToUpdate.getQuantity());
		costPrice.setText("" + productToUpdate.getCost_price());
		sellingPrice.setText("" + productToUpdate.getSelling_price());
		description.setText(productToUpdate.getDescription());
		imageInByte = productToUpdate.getImg();
		Showimage();

		System.out.println(imageInByte);
		listChoiceBoxUnit(productToUpdate.getUnit_id());
		listChoiceBoxCategory(productToUpdate.getCategory_id());

	}

	private void Showimage() {
		ByteArrayInputStream is = new ByteArrayInputStream(imageInByte);
		imageProduct.setImage(new Image(is));

	}

	public void initData(ShowProductsController ProductController,
			Product productToUpdate, int index) {
		this.productController = ProductController;
		this.productToUpdate = productToUpdate;
		this.index = index;
		initForm();
	}

}
