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
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.Choice;
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
	private ChoiceBox<Choice> unit;
	private Stage stage;
	private Product product;
	private ObservableList<Choice> listChoiceUnit;
	private ObservableList<Choice> listChoiceCategory;
	private byte[] imageInByte;
	private ShowProductsController productController;
	private boolean verifUpc;
	private boolean error;

	private String errorMsg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listChoiceBoxUnit();
		listChoiceBoxCategory();
		FXUtils.addDraggableNode(titleBar);

		btnAddImg.setOnAction(e -> {
			addPicture();
		});

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
					error = false;
					verifUpc = false;
					String upcStr = upc.getText().trim();
					String nameStr = name.getText().trim();
					String quantityStr = quantity.getText().trim();
					String costPriceStr = costPrice.getText().trim();
					String sellingPriceStr = sellingPrice.getText().trim();
					String descriptionStr = description.getText().trim();
					errorMsg = "";
					for (ProductTV b : productController
							.getProductsObservableList()) {

						if (b.getUpc().equals(upcStr))
							verifUpc = true;

					}

					if (upcStr.isEmpty() || nameStr.isEmpty()
							|| quantityStr.isEmpty() || costPriceStr.isEmpty()) {
						errorMsg += "Tous les champs avec une etoile sont requis\n";
						error = true;
					}
					if (!upcStr
							.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
						errorMsg += "Le upc doit avoir 12 chiffres\n ";
						error = true;
					}
					if (verifUpc == true) {
						upc.setStyle(FXUtils.HAS_ERROR);
						errorMsg += "Le upc existe déja, veuillez saisir un autre upc\n";
						error = true;
					}
					if (nameStr.isEmpty()) {
						errorMsg += "Veuillez saisir un nom de produit\n";
						error = true;
					} else if (nameStr.length() > 50) {
						errorMsg += "Le nom du produit ne doit pas depasse 50 caracteres\n";
						error = true;
					}
					if (!StringUtils.isInteger(quantityStr)) {
						errorMsg += "Veuillez saisir une quantité valide\n";
						error = true;
					} else if (Integer.parseInt(quantityStr) < 0) {
						errorMsg += "Veuillez saisir une quantité valide\n";
						error = true;
					}
					if (!StringUtils.validDecimal((costPriceStr))) {
						errorMsg += "Veuillez saisir un prix coutant valide\n";
						error = true;
					}
					if (!sellingPriceStr.isEmpty()) {
						if (!StringUtils.validDecimal((sellingPriceStr))) {
							errorMsg += "Veuillez saisir un prix vendant valide\n";
							error = true;
						} else if (!error) {
							double costprice = Double.parseDouble(costPriceStr);
							double minSellingPrice = costprice
									+ (costprice * 10) / 100;
							if (Double.parseDouble(sellingPriceStr) < minSellingPrice) {
								errorMsg += "Le prix ajuste ne peut etre plus petit que : "
										+ minSellingPrice + " $.\n";
								error = true;
							}

						}
					}
					if (descriptionStr.length() > 255) {
						errorMsg += "La description ne doit pas depasse 255 caractères";
						error = true;
					}
					stage = (Stage) btnAddProduct.getScene().getWindow();
					if (!error) {
						product = new Product();
						product.setUpc(upcStr);
						product.setName(nameStr);
						product.setQuantity(Integer.parseInt(quantityStr));
						product.setUnit_id(unit.getSelectionModel()
								.getSelectedItem().getId());
						product.setCategory_id(category.getSelectionModel()
								.getSelectedItem().getId());
						product.setImg(imageInByte);
						product.setDescription(descriptionStr);
						product.setCost_price(Double.parseDouble(costPriceStr));
						if (!sellingPriceStr.isEmpty())
							product.setSelling_price(Double
									.parseDouble(sellingPriceStr));
						ProductManager.add(product);
						addProductToTableView();
						stage.close();
						productController.showTableView();
						productController.selectLastRow();
					} else {
						try {
							new CustomInfoBox(stage, BoxType.INFORMATION,
									errorMsg, "Ok");
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

	private void listChoiceBoxUnit() {
		listChoiceUnit = FXCollections.observableArrayList();
		for (Unit unit : UnitManager.getAll()) {
			listChoiceUnit.add(new Choice(unit.getId(), unit.getDescription()));
		}
		unit.setItems(listChoiceUnit);
		unit.getSelectionModel().select(0);
	}

	private void listChoiceBoxCategory() {
		listChoiceCategory = FXCollections.observableArrayList();
		for (Category category : CategoryManager.getAll()) {
			listChoiceCategory.add(new Choice(category.getId(), category
					.getDescription()));
		}

		category.setItems(listChoiceCategory);
		category.getSelectionModel().select(0);

	}

	private void addProductToTableView() {
		ProductTV productTableView = new ProductTV();
		productTableView.setId(product.getId());
		productTableView.setUpc(product.getUpc());
		productTableView.setName(product.getName());
		productTableView.setQuantity(product.getQuantity());
		productTableView.setSelling_price(product.getSelling_price());
		productTableView.setDescriptionCategory(category.getSelectionModel()
				.getSelectedItem().toString());

		productController.addToTableView(productTableView);

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
					stage = (Stage) btnAddProduct.getScene().getWindow();
					try {
						new CustomInfoBox(
								stage,
								BoxType.INFORMATION,
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
