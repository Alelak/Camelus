package com.devsolutions.camelus.controllers;

import java.awt.image.BufferedImage;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import util.Choice;

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;

public class ShowProductsController implements Initializable {

	@FXML
	private TextField upc;
	@FXML
	private Button btnAddProduct;
	@FXML
	private Button btnAddUnit;
	@FXML
	private Button btnAddImg;
	@FXML
	private TextField name;
	@FXML
	private TextField quantity;
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listChoiceBoxUnit();
		listChoiceBoxCategory();
		btnAddImg.setOnAction(e -> {
			addPicture();
		});
		btnAddProduct.setOnAction(e -> {
			ProductManager.add(addProduct());
		});

	}

	public void listChoiceBoxUnit() {
		List<Unit> unitList = UnitManager.getAll();
		listChoiceUnit = FXCollections.observableArrayList();
		listChoiceUnit.add(new Choice(0, "No Selection"));
		for (Unit unit : unitList) {
			listChoiceUnit.add(new Choice(unit.getId(), unit.getDescription()));
		}
		unit.setItems(listChoiceUnit);
		unit.getSelectionModel().select(0);
	}

	public void listChoiceBoxCategory() {
		List<Category> CategoryList = CategoryManager.getAll();
		listChoiceCategory = FXCollections.observableArrayList();
		listChoiceCategory.add(new Choice(0, "No Selection"));
		for (Category category : CategoryList) {
			listChoiceCategory.add(new Choice(category.getId(), category
					.getDescription()));
		}

		category.setItems(listChoiceCategory);
		category.getSelectionModel().select(0);

	}

	public Product addProduct()

	{

		Product product = new Product();

		product.setUpc(Integer.parseInt(upc.getText()));
		product.setName(name.getText());
		product.setQuantity(Integer.parseInt(quantity.getText()));
		product.setUnit_id(unit.getSelectionModel().getSelectedItem().getId());
		product.setCategory_id(category.getSelectionModel().getSelectedItem()
				.getId());
		product.setImg(imageInByte);
		product.setDescription(description.getText());

		return product;

	}

	public void addPicture() {

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"All Images", "*.*");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(stage);
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
