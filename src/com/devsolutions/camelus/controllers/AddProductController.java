package com.devsolutions.camelus.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import com.devsolutions.camelus.utils.CustomInfoBox;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;

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

	private String errorMsg;
	private String errorMsgImg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listChoiceBoxUnit();
		listChoiceBoxCategory();
		errorMsg = "";
		errorMsgImg = "";
		FXUtils.addDraggableNode(titleBar);

		btnAddImg.setOnAction(e -> {		
				addPicture();
		});

		upc.setOnKeyReleased(e -> {
			if (upc.getText()
					.matches(
							"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
				upc.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");

			} else
				upc.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
		});

		quantity.setOnKeyReleased(e -> {
			if (StringUtils.isInteger(quantity.getText())) {
				if (Double.parseDouble(quantity.getText()) > 0)
					quantity.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");
			} else
				quantity.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");

		});

		costPrice
				.setOnKeyReleased(e -> {
					if (StringUtils.isDouble(costPrice.getText())) {

						if (Double.parseDouble(costPrice.getText()) > 0)
							costPrice
									.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");

					} else
						costPrice
								.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
				});
		sellingPrice
				.setOnKeyReleased(e -> {
					if (StringUtils.isDouble(sellingPrice.getText())) {

						if (Double.parseDouble(sellingPrice.getText()) > 0)
							sellingPrice
									.setStyle("-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;");

					} else
						sellingPrice
								.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
				});

		upc.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (!newPropertyValue) {
					if (upc.getText().isEmpty())
						upc.setStyle("-fx-border-width: 0;");
					if (upc.getText()
							.matches(
									"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
						upc.setStyle("-fx-border-width: 0;");
					}
				}
			}
		});

		quantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (!newPropertyValue) {
					if (quantity.getText().isEmpty())
						quantity.setStyle("-fx-border-width: 0;");
					if (StringUtils.isInteger(quantity.getText())) {
						quantity.setStyle("-fx-border-width: 0;");
					}
				}
			}
		});
		costPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (!newPropertyValue) {
					if (costPrice.getText().isEmpty())
						costPrice.setStyle("-fx-border-width: 0;");
					if (StringUtils.isDouble(costPrice.getText())) {

						if (Double.parseDouble(costPrice.getText()) > 0)
							costPrice.setStyle("-fx-border-width: 0;");
					}
				}
			}
		});
		sellingPrice.focusedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(
							ObservableValue<? extends Boolean> arg0,
							Boolean oldPropertyValue, Boolean newPropertyValue) {
						if (!newPropertyValue) {
							if (sellingPrice.getText().isEmpty())
								sellingPrice.setStyle("-fx-border-width: 0;");
							if (StringUtils.isDouble(sellingPrice.getText())) {

								if (Double.parseDouble(sellingPrice.getText()) > 0)
									sellingPrice
											.setStyle("-fx-border-width: 0;");
							}
						}
					}
				});
		btnAddProduct
				.setOnAction(e -> {
					for (ProductTableView b : productController
							.getProductsObservableList()) {

						if (b.getUpc().equals(upc.getText()))
							verifUpc = true;

					}

					if (!(!upc.getText().isEmpty() && !name.getText().isEmpty()
							&& !quantity.getText().isEmpty() && !costPrice
							.getText().isEmpty())) {
						errorMsg += "- il faut remplir au minimum UPC, Name, Prix coutant, Quantiter SVP \n";
						error = true;
					}
					if (!upc.getText()
							.matches(
									"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
						errorMsg += "- le UPC doit avoir 12 chiffres \n ";
						error = true;
					}
					if (verifUpc == true) {
						upc.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
						errorMsg += "- le UPC existe deja veillez entrer un nouveau upc svp\n";
						error = true;
					}
					if (name.getText().equals("")) {
						errorMsg += "- Veillez saisire le nom de produit SVP\n";
						error = true;
					}
					if (!StringUtils.isInteger(quantity.getText())) {
						errorMsg += "- entrer une Quantiter valide\n";
						error = true;
					} else if (Integer.parseInt(quantity.getText()) < 0) {
						errorMsg += "- Veuillez entrez une qunatiter Posetive SVP\n";
						error = true;
					}
					if (!StringUtils.isDouble(costPrice.getText())) {
						errorMsg += "- saisie un prix valide Svp\n";
						error = true;
					} else if (Double.parseDouble(costPrice.getText()) < 0) {
						errorMsg += "- saisie un prix Posetive Svp\n";
						error = true;
					}
					stage = (Stage) btnAddProduct.getScene().getWindow();
					if (error == false) {

						ProductManager.add(addProduct());
						addProductToTableView();
						stage.close();
						productController.showTableView();
						productController.selectLastRow();
					} else {
						try {
							CustomInfoBox customDialogBox = new CustomInfoBox(
									stage, errorMsg, "Ok", "#282828");
							customDialogBox.btn
									.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											stage = (Stage) customDialogBox.btn
													.getScene().getWindow();
											errorMsg = "";
											stage.close();
										}
									});
						} catch (IOException ex) {
							ex.printStackTrace();
						}

					}
				});
		btnCancelProduct.setOnAction(e -> {
			stage = (Stage) btnCancelProduct.getScene().getWindow();
			stage.close();
		});

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
		if (!sellingPrice.getText().isEmpty())
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
				"All Images", "*.jpg", "*.jpeg", "*.gif", "*.bmp");
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
					Showimage();
				} else {
					errorMsgImg = "La taille de l`image est très grande veuillez choisie une autre image svp";
					stage = (Stage) btnAddProduct.getScene().getWindow();
					try {
						CustomInfoBox customDialogBox = new CustomInfoBox(
								stage, errorMsgImg, "Ok", "#282828");
						customDialogBox.btn
								.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										stage = (Stage) customDialogBox.btn
												.getScene().getWindow();
										errorMsgImg = "";
										stage.close();
									}
								});
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void initData(ShowProductsController ProductController) {
		this.productController = ProductController;
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
