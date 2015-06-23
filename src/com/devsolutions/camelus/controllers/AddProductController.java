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

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTableView;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.utils.Choice;

public class AddProductController implements Initializable {

	@FXML
	private TextField upc;
	@FXML
	private Button btnCancelProduct;
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
	Product product;
	private ByteArrayOutputStream baos = null;
	private ObservableList<Choice> listChoiceUnit;
	private ObservableList<Choice> listChoiceCategory;
	private byte[] imageInByte;
	private ShowProductsController productController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listChoiceBoxUnit();
		listChoiceBoxCategory();
		btnAddImg.setOnAction(e -> {
			addPicture();
		});
		btnAddProduct.setOnAction(e -> {
			if ((unit.getSelectionModel().getSelectedIndex() == 0)
					|| (category.getSelectionModel().getSelectedIndex() == 0))
				System.out.println("il faut choisir une unit ou une category");

			if (!upc.getText().isEmpty() && !name.getText().isEmpty()
					&& !quantity.getText().isEmpty() && imageInByte != null
					&& !description.getText().isEmpty()
					&& !sellingPrice.getText().isEmpty()
					&& !costPrice.getText().isEmpty()) {
				ProductManager.add(addProduct());
				addProductToTableView();
				stage = (Stage) btnAddProduct.getScene().getWindow();
				stage.close();
			} else
				System.out.println("tous les chams doivent etre remplis");

		});
		btnCancelProduct.setOnAction(e -> {
			stage = (Stage) btnCancelProduct.getScene().getWindow();
			stage.close();
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

		product = new Product();

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
		System.out.println("test table view" + product.getCategory_id());

		return product;
	}

	public void addProductToTableView() {
		ProductTableView productTableView = new ProductTableView();
		productTableView.setId(product.getId());
		productTableView.setUpc(product.getUpc());
		productTableView.setName(product.getName());
		productTableView.setQuantity(product.getQuantity());
		productTableView.setSelling_price(product.getSelling_price());
		productTableView.setDescriptionCategory(category.getSelectionModel()
				.getSelectedItem().toString());
		System.out.println("la valeur de choice box category = "
				+ productTableView.getDescriptionCategory());
		productController.addToTableView(productTableView);

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

	public void initData(ShowProductsController ProductController) {
		this.productController = ProductController;
	}

}
