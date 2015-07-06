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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
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
	private ImageView imageProduct;
	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
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
	private boolean verifUpc = false;
	private boolean error = false;
	private double initialX;
	private double initialY;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listChoiceBoxUnit();
		listChoiceBoxCategory();
		addDraggableNode(titleBar);

		btnAddImg.setOnAction(e -> {
			addPicture();
			 Showimage();
			
		});

		upc.setOnKeyReleased(e -> {
			if (upc.getText()
					.matches(
							"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
				upc.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");
			
				
			} else
				upc.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
		});
		name.setOnKeyReleased(e -> {
			if (name.getText().isEmpty()) {
				name.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
			
				
			} else
				name.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");
		});
		quantity.setOnKeyReleased(e -> {
			if (isNumeric(quantity.getText())){
				if  (Double.parseDouble(quantity.getText()) > 0)
					quantity.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");
			}else
				quantity.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
			
		});
	
		costPrice.setOnKeyReleased(e -> {
			if (isNumber(costPrice.getText())){
				
		if (Double.parseDouble(costPrice.getText()) > 0)
				costPrice.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");

			}	else
				costPrice.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
		});
		
		btnAddProduct
				.setOnAction(e -> {
					/*
					 * if ((unit.getSelectionModel().getSelectedIndex() == 0) ||
					 * (category.getSelectionModel().getSelectedIndex() == 0))
					 * System.out
					 * .println("il faut choisir une unit ou une category");
					 */

					for (ProductTableView b : productController
							.getProductsList()) {
						System.out.println("verification UpC avant");
						if (b.getUpc().equals(upc.getText()))
							verifUpc = true;
						System.out.println("verification UpC");

					}

					if (!(!upc.getText().isEmpty() && !name.getText().isEmpty()
							&& !quantity.getText().isEmpty() && !costPrice
							.getText().isEmpty())) {
						System.out
								.println("il faut remplir au minimum UPC, Name, Prix coutant, Quantiter SVP");
						error = true;
					}
					if (!upc.getText()
							.matches(
									"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
						System.out.println("le UPC doit avoir 12 chiffres ");
						error = true;
					}
					if (verifUpc == true) {
						upc.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
						System.out
								.println("le UPC existe deja veillez entrer un nouveau upc svp");
						error = true;
					}
					if (name.getText().equals("")) {
						System.out
								.println("Veillez saisire le nom de produit SVP");
						error = true;
					}
					if (!isNumeric(quantity.getText())) {
						System.out.println("entrer une Quantiter valide");
						error = true;
					} else if (Integer.parseInt(quantity.getText()) < 0) {
						System.out
								.println("Veuillez entrez une qunatiter Posetive SVP");
						error = true;
					}
					if (!isNumber(costPrice.getText())) {
						System.out.println("saisie un prix valide Svp");
						error = true;
					} else if (Double.parseDouble(costPrice.getText()) < 0) {
						System.out.println("saisie un prix Posetive Svp");
						error = true;
					}
					if (error == false) {

						ProductManager.add(addProduct());
						addProductToTableView();
						stage = (Stage) btnAddProduct.getScene().getWindow();
						stage.close();
					}

				});
		btnCancelProduct.setOnAction(e -> {
			stage = (Stage) btnCancelProduct.getScene().getWindow();
			stage.close();
		});

	}

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public void listChoiceBoxUnit() {
		List<Unit> unitList = UnitManager.getAll();
		listChoiceUnit = FXCollections.observableArrayList();
		for (Unit unit : unitList) {
			listChoiceUnit.add(new Choice(unit.getId(), unit.getDescription()));
		}
		unit.setItems(listChoiceUnit);
		unit.getSelectionModel().select(0);
	}

	public void listChoiceBoxCategory() {
		List<Category> CategoryList = CategoryManager.getAll();
		listChoiceCategory = FXCollections.observableArrayList();
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
		if(!sellingPrice.getText().isEmpty())
		product.setSelling_price(Double.parseDouble(sellingPrice.getText()));

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

		productController.addToTableView(productTableView);

	}

	public void addPicture() {

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"All Images", "*.jpg","*.png","*.jpeg","*.gif","*.bmp");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			try {
				double mb = file.length() / 1048576;

				if (mb < 1) {
					BufferedImage originalImage = ImageIO.read(file);
					baos = new ByteArrayOutputStream();
					ImageIO.write(originalImage, "jpg", baos);
					baos.flush();
					imageInByte = baos.toByteArray();
				} else
					System.out.println(mb);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void initData(ShowProductsController ProductController) {
		this.productController = ProductController;
	}

	private void addDraggableNode(final Node node) {
		node.setOnMousePressed(e -> {
			if (e.getButton() != MouseButton.MIDDLE) {
				initialX = e.getSceneX();
				initialY = e.getSceneY();
			}
		});
		node.setOnMouseDragged(e -> {
			if (e.getButton() != MouseButton.MIDDLE) {
				node.getScene().getWindow().setX(e.getScreenX() - initialX);
				node.getScene().getWindow().setY(e.getScreenY() - initialY);
			}
		});
	}
	private void Showimage() {

		ByteArrayInputStream is = new ByteArrayInputStream(imageInByte);
		imageProduct.setImage(new Image(is));

	}

	@FXML
	private void CloseWindow() {

		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}
}
