package com.devsolutions.camelus.views;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import com.devsolutions.camelus.entities.ProductTV;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.utils.Choice;
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
	private ChoiceBox<Choice> unit;
	private Stage stage;

	private ObservableList<Choice> listChoiceUnit;
	private ObservableList<Choice> listChoiceCategory;
	private byte[] imageInByte;
	private ShowProductsController productController;
	private Product product;
	private Product productToUpdate;
	private int index;
	private boolean verifUpc;
	private boolean error;

	private String errorMsg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		upc.setOnKeyReleased(e -> {
			if (upc.getText()
					.matches(
							"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
				upc.setStyle(FXUtils.HAS_SUCCESS);

			} else
				upc.setStyle(FXUtils.HAS_ERROR);
		});

		quantity.setOnKeyReleased(e -> {
			if (StringUtils.isInteger(quantity.getText())) {
				if (Double.parseDouble(quantity.getText()) > 0)
					quantity.setStyle(FXUtils.HAS_SUCCESS);
			} else
				quantity.setStyle(FXUtils.HAS_ERROR);

		});

		costPrice.setOnKeyReleased(e -> {
			if (StringUtils.isDouble(costPrice.getText())) {

				if (Double.parseDouble(costPrice.getText()) > 0)
					costPrice.setStyle(FXUtils.HAS_SUCCESS);

			} else
				costPrice.setStyle(FXUtils.HAS_ERROR);
		});
		sellingPrice.setOnKeyReleased(e -> {
			if (StringUtils.isDouble(sellingPrice.getText())) {

				if (Double.parseDouble(sellingPrice.getText()) > 0)
					sellingPrice.setStyle(FXUtils.HAS_SUCCESS);

			} else
				sellingPrice.setStyle(FXUtils.HAS_ERROR);
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
					errorMsg = "";
					String upcStr = upc.getText().trim();
					String nameStr = name.getText().trim();
					String quantityStr = quantity.getText().trim();
					String costPriceStr = costPrice.getText().trim();
					String sellingPriceStr = sellingPrice.getText().trim();
					for (ProductTV b : productController
							.getProductsObservableList()) {

						if (b.getUpc().equals(upcStr))
							verifUpc = true;

					}

					if (!(!upcStr.isEmpty() && !nameStr.isEmpty()
							&& !quantityStr.isEmpty() && !costPriceStr
							.isEmpty())) {
						errorMsg += "Tous les champs avec une etoile sont requis\n";
						error = true;
					}
					if (!upcStr
							.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
						errorMsg += "Le upc doit avoir 12 chiffres \n";
						error = true;
					}
					if ((verifUpc == true)
							&& (!upcStr.equals(productToUpdate.getUpc()))) {
						upc.setStyle(FXUtils.HAS_ERROR);
						errorMsg += "Le upc existe deja, veuillez saisir un autre upc\n";
						error = true;
					}
					if (nameStr.isEmpty()) {
						errorMsg += "Veuillez saisir un nom de produit\n";
						error = true;
					}
					if (!StringUtils.isInteger(quantityStr)) {
						errorMsg += "Veuillez saisir une quantite valide\n";
						error = true;
					} else if (Integer.parseInt(quantityStr) < 0) {
						errorMsg += "Veuillez saisir une quantite valide\n";
						error = true;
					}
					if (!StringUtils.isDouble(costPriceStr)) {
						errorMsg += "Veuillez saisir un prix coutant valide\n";
						error = true;
					} else if (Double.parseDouble(costPriceStr) < 0) {
						errorMsg += "Veuillez saisir un prix coutant valide\n";
						error = true;
					}
					if (!sellingPriceStr.isEmpty()) {
						if (!StringUtils.isDouble(sellingPriceStr)) {
							errorMsg += "Veuillez saisir un prix vendant valide\n";
							error = true;
						} else if (Double.parseDouble(sellingPriceStr) < 0) {
							errorMsg += "Veuillez saisir un prix vendant valide\n";
							error = true;
						}
					}
					stage = (Stage) btnUpdateProduct.getScene().getWindow();
					if (error == false) {
						product = new Product();
						product.setId(productToUpdate.getId());
						product.setUpc(upcStr);
						product.setName(nameStr);
						product.setQuantity(Integer.parseInt(quantityStr));
						product.setUnit_id(unit.getSelectionModel()
								.getSelectedItem().getId());
						product.setCategory_id(category.getSelectionModel()
								.getSelectedItem().getId());
						product.setImg(imageInByte);
						product.setDescription(description.getText());
						product.setCost_price(Double.parseDouble(costPriceStr));
						if (!sellingPriceStr.isEmpty())
							product.setSelling_price(Double
									.parseDouble(sellingPriceStr));
						ProductManager.update(product);
						ProductTV productTable = new ProductTV();
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
							new CustomInfoBox(stage, errorMsg, "Ok");
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

	private void listChoiceBoxUnit(int index) {
		listChoiceUnit = FXCollections.observableArrayList();
		for (Unit unit : UnitManager.getAll()) {
			listChoiceUnit.add(new Choice(unit.getId(), unit.getDescription()));
		}
		unit.setItems(listChoiceUnit);
		unit.getSelectionModel().select(index);
	}

	private void listChoiceBoxCategory(int index) {
		listChoiceCategory = FXCollections.observableArrayList();
		for (Category category : CategoryManager.getAll()) {
			listChoiceCategory.add(new Choice(category.getId(), category
					.getDescription()));
		}

		category.setItems(listChoiceCategory);
		category.getSelectionModel().select(index);

	}

	private void addPicture() {
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
		upc.setText(productToUpdate.getUpc());
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
					"/images/nopicture.jpg")));
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
