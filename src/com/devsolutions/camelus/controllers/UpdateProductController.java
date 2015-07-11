package com.devsolutions.camelus.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

public class UpdateProductController implements Initializable {
	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
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

	private ObservableList<Choice> listChoiceUnit;
	private ObservableList<Choice> listChoiceCategory;
	private byte[] imageInByte;
	private ShowProductsController productController;
	Product product;
	Product productToUpdate;
	int index;
	private boolean verifUpc = false;
	private boolean error = false;

	private String errorMsg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		errorMsg = "";
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

		btnAddImg.setOnAction(e -> {
			addPicture();

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
		btnUpdateProduct
				.setOnAction(e -> {

					error = false;
					verifUpc = false;
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
						errorMsg += "le UPC doit avoir 12 chiffres ";
						error = true;
					}
					if ((verifUpc == true)
							&& (!upc.getText().equals(productToUpdate.getUpc()))) {
						upc.setStyle("-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;");
						errorMsg += "- le UPC existe deja veillez entrer un nouveau upc svp \n";
						error = true;
					}
					if (name.getText().equals("")) {
						errorMsg += "- Veillez saisire le nom de produit SVP \n";
						error = true;
					}
					if (!StringUtils.isInteger(quantity.getText())) {
						errorMsg += "- entrer une Quantiter valide \n";
						error = true;
					} else if (Integer.parseInt(quantity.getText()) < 0) {
						errorMsg += "- Veuillez entrez une qunatiter Posetive SVP\n";
						error = true;
					}
					if (!StringUtils.isDouble(costPrice.getText())) {
						errorMsg += "- saisie un prix valide Svp \n";
						error = true;
					} else if (Double.parseDouble(costPrice.getText()) < 0) {
						errorMsg += "- saisie un prix Posetive Svp\n";
						error = true;
					}
					stage = (Stage) btnUpdateProduct.getScene().getWindow();
					if (error == false) {

						ProductManager.update(updateProduct());
						ProductTableView productTable = new ProductTableView();
						productTable.setId(product.getId());

						productTable.setName(product.getName());
						productTable.setQuantity(product.getQuantity());
						productTable.setUpc(product.getUpc());
						productTable.setSelling_price(product
								.getSelling_price());
						productTable.setDescriptionCategory(category.getValue()
								.toString());
						productController.updateTableView(index, productTable);

						stage.close();
						productController.selectTheModifierRow(index);
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
		File defaultDirectory = new File(System.getProperty("user.home")
				+ "/Pictures");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(defaultDirectory);
		fileChooser.setTitle("Choisir une image");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"All Images", "*.jpg", "*.png", "*.jpeg", "*.gif", "*.bmp");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			try {
				double mb = file.length() / 1048576;

				if (mb < 1) {
					Path path = Paths.get(file.getAbsolutePath());
					imageInByte = Files.readAllBytes(path);
					Showimage();
					btnAddImg.setText("Modifier Image");
				} else {
					stage = (Stage) btnUpdateProduct.getScene().getWindow();
					try {
						new CustomInfoBox(
								stage,
								"Choisissez une image avec une taille de 1MO ou moins",
								"Ok");

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

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
		if (imageInByte != null)
			Showimage();
		else {
			imageProduct.setImage(new Image(getClass().getResourceAsStream(
					"../../../../images/nopicture.jpg")));
		}

		listChoiceBoxUnit(productToUpdate.getUnit_id());
		listChoiceBoxCategory(productToUpdate.getCategory_id());

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

	public void initData(ShowProductsController ProductController,
			Product productToUpdate, int index) {
		this.productController = ProductController;
		this.productToUpdate = productToUpdate;
		this.index = index;
		initForm();
	}

}
