package com.devsolutions.camelus.views;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTV;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class ShowProductsController implements Initializable {

	@FXML
	private GridPane motherGrid;
	@FXML
	private GridPane content;
	@FXML
	private RowConstraints rowOne;
	@FXML
	private VBox gridRowOne;
	@FXML
	private Label message1;
	@FXML
	private RowConstraints rowTwo;
	@FXML
	private GridPane gridRowTwo;
	@FXML
	private TextField textFieldSearch;

	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnAddProduct;
	@FXML
	private Button btnUpdateProduct;
	@FXML
	private Button btnDeleteProduct;
	@FXML
	private Button btnPdfProduct;
	@FXML
	private Button btnShowProduct;
	@FXML
	private TableView<ProductTV> tableViewProduct;
	private ObservableList<ProductTV> productsObservableList;
	@FXML
	private TableColumn<ProductTV, Long> idCol;
	@FXML
	private TableColumn<ProductTV, String> upcCol;
	@FXML
	private TableColumn<ProductTV, String> nameCol;
	@FXML
	private TableColumn<ProductTV, Integer> quantityCol;
	@FXML
	private TableColumn<ProductTV, String> categoryCol;
	@FXML
	private TableColumn<ProductTV, Double> priceSellingcol;
	@FXML
	private SortedList<ProductTV> sortedData;
	private Stage stage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(false);
		initTableView();
		if (Session.vendor != null) {
			btnAddProduct.setDisable(true);
		}

		if (productsObservableList.size() == 0) {

			noDataToShow();
			btnPdfProduct.setDisable(true);
		} else {
			showTableView();
			btnPdfProduct.setDisable(false);
		}
		FilteredList<ProductTV> filteredData = new FilteredList<>(
				productsObservableList, p -> true);

		textFieldSearch.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(ProductTableView -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}

						String lowerCaseFilter = newValue.toLowerCase();
						String id = ProductTableView.getId() + "";

						if (ProductTableView.getUpc().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (ProductTableView.getName().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (ProductTableView.getDescriptionCategory()
								.toLowerCase().contains(lowerCaseFilter)) {
							return true;
						} else if (id.contains(lowerCaseFilter)) {
							return true;
						}

						return false;
					});
				});

		sortedData = new SortedList<>(filteredData);

		sortedData.comparatorProperty().bind(
				tableViewProduct.comparatorProperty());

		tableViewProduct.setItems(sortedData);

		btnAddProduct
				.setOnAction(e -> {
					Stage stage = new Stage();
					Parent root = null;
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"AddProduct.fxml"));
					try {
						root = loader.load();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Scene scene = new Scene(root);
					scene.getStylesheets().add(
							getClass().getResource("childWindow.css")
									.toExternalForm());
					AddProductController controller = loader
							.<AddProductController> getController();
					controller.initData(this);
					stage.setScene(scene);
					stage.initStyle(StageStyle.UNDECORATED);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.initOwner(motherGrid.getScene().getWindow());
					stage.show();
					FXUtils.centerStage((Stage) motherGrid.getScene().getWindow(), stage, 22);
				});

		btnUpdateProduct
				.setOnAction(e -> {
					int index = tableViewProduct.getSelectionModel()
							.getSelectedIndex();
					if (index > -1) {
						ProductTV productTableView = tableViewProduct
								.getSelectionModel().getSelectedItem();

						Product product = ProductManager
								.getById(productTableView.getId());

						if (product != null) {
							Stage stage = new Stage();
							Parent root = null;
							FXMLLoader loader = new FXMLLoader(getClass()
									.getResource("UpdateProduct.fxml"));
							try {
								root = loader.load();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Scene scene = new Scene(root);
							scene.getStylesheets().add(
									getClass().getResource("childWindow.css")
											.toExternalForm());
							UpdateProductController controller = loader
									.<UpdateProductController> getController();
							controller.initData(this, product, index);

							stage.setScene(scene);
							stage.initStyle(StageStyle.UNDECORATED);
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.initOwner(motherGrid.getScene().getWindow());
							stage.show();
							FXUtils.centerStage((Stage) motherGrid.getScene().getWindow(), stage, 22);
						}
					}
				});
		btnShowProduct
				.setOnAction(e -> {

					int index = tableViewProduct.getSelectionModel()
							.getSelectedIndex();
					if (index > -1) {

						ProductTV productTableView = tableViewProduct
								.getSelectionModel().getSelectedItem();

						Product product = ProductManager
								.getById(productTableView.getId());

						if (product != null) {
							Stage stage = new Stage();
							Parent root = null;
							FXMLLoader loader = new FXMLLoader(getClass()
									.getResource("ShowProduct.fxml"));
							try {
								root = loader.load();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Scene scene = new Scene(root);
							scene.getStylesheets().add(
									getClass().getResource("childWindow.css")
											.toExternalForm());
							ShowProductController controller = loader
									.<ShowProductController> getController();
							controller.initData(this, product, index);

							stage.setScene(scene);
							stage.initStyle(StageStyle.UNDECORATED);
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.initOwner(motherGrid.getScene().getWindow());
							stage.show();
							FXUtils.centerStage((Stage) motherGrid.getScene().getWindow(), stage, 22);
						}
					}
				});

		btnDeleteProduct
				.setOnAction(e -> {

					try {
						CustomDialogBox customDialogBox = new CustomDialogBox(
								(Stage) btnDeleteProduct.getScene().getWindow(),
								BoxType.WARNING,
								"Voulez vous vraiment supprimer "
										+ tableViewProduct.getSelectionModel()
												.getSelectedItem().getName()
										+ " de votre liste de Produits?",
								"Oui", "Non");

						customDialogBox.positiveButton
								.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										ProductTV productTable = tableViewProduct
												.getSelectionModel()
												.getSelectedItem();
										if (productTable != null) {
											ProductManager.delete(productTable
													.getId());

											productsObservableList
													.remove(productTable);
											if (productsObservableList.size() == 0) {
												btnPdfProduct.setDisable(true);
											}
										}

										if (productsObservableList.isEmpty()) {
											noDataToShow();
										}
										Stage dialogBoxStage = (Stage) customDialogBox.positiveButton
												.getScene().getWindow();
										dialogBoxStage.close();
									}
								});
					} catch (IOException e2) {
						e2.printStackTrace();
					}

				});
		btnPdfProduct.setOnAction(e -> {
			creatPDF();
		});
		tableViewProduct.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						if (Session.vendor == null) {
							btnDeleteProduct.setDisable(false);

							btnUpdateProduct.setDisable(false);
						}
						btnShowProduct.setDisable(false);

					} else {
						btnDeleteProduct.setDisable(true);
						btnShowProduct.setDisable(true);
						btnUpdateProduct.setDisable(true);

					}
				});
		btnRefresh.setOnAction(e -> {
			productsObservableList.clear();
			productsObservableList.addAll(ProductManager
					.getAllProductTableView());
		});
	}

	private void noDataToShow() {
		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(true);
		rowOne.setPercentHeight(100);
		rowTwo.setPercentHeight(0);
	}

	public void showTableView() {
		gridRowOne.setVisible(false);
		gridRowTwo.setVisible(true);
		rowOne.setPercentHeight(0);
		rowTwo.setPercentHeight(100);
	}

	private void initTableView() {

		productsObservableList = FXCollections
				.observableArrayList(ProductManager.getAllProductTableView());

		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		upcCol.setCellValueFactory(new PropertyValueFactory<>("upc"));

		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>(
				"descriptionCategory"));
		priceSellingcol.setCellValueFactory(new PropertyValueFactory<>(
				"selling_price"));

	}

	public void selectLastRow() {
		int indexLastRow = productsObservableList.size() - 1;
		tableViewProduct.getSelectionModel().select(indexLastRow);
		tableViewProduct.getFocusModel().focus(indexLastRow);
	}

	public void selectTheModifierRow(int index) {
		tableViewProduct.getSelectionModel().select(index);
		tableViewProduct.getFocusModel().focus(index);
	}

	public void addToTableView(ProductTV product) {
		productsObservableList.add(product);
	}

	public void updateTableView(int index, ProductTV product) {
		productsObservableList.set(index, product);
	}

	public ObservableList<ProductTV> getProductsObservableList() {
		return productsObservableList;
	}

	private void creatPDF() {
		Document document = new Document();
		String timeStamp = StringUtils.formatDate(new Date());
		stage = (Stage) btnPdfProduct.getScene().getWindow();

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Exporter le catalogue des produits");
		String defaultFileName = "catalogue-de-produit" + "(" + timeStamp
				+ ").pdf";
		fileChooser.setInitialFileName(defaultFileName);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"PDF Files", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);
		File savedFile = fileChooser.showSaveDialog(stage);
		Font boldFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
		if (savedFile != null) {

			try {
				PdfWriter.getInstance(document,
						new FileOutputStream(savedFile.getAbsoluteFile()));

				document.setPageSize(PageSize.A4);
				document.setMargins(0, 0, 30, 0);
				document.open();

				Paragraph p1 = new Paragraph();
				p1.setIndentationLeft(50f);
				p1.setIndentationRight(50f);
				p1.setSpacingAfter(30f);

				try {

					Image image = Image.getInstance(getClass().getResource(
							"/images/gfc.png"));

					Paragraph imageParagraph = new Paragraph();
					imageParagraph.add(image);
					imageParagraph.setIndentationLeft(20f);

					PdfPTable tabHeader = new PdfPTable(2);
					tabHeader.setWidthPercentage(100);

					PdfPCell cell1 = new PdfPCell(image, true);
					cell1.setBorder(Rectangle.NO_BORDER);
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					tabHeader.addCell(cell1);

					PdfPCell cell2 = new PdfPCell();
					Paragraph parDate = new Paragraph(new Phrase(
							"                                                    Date: "
									+ timeStamp, boldFont));
					parDate.setAlignment(Element.ALIGN_RIGHT);
					parDate.setSpacingAfter(50f);
					parDate.setSpacingBefore(50f);
					parDate.setIndentationRight(20f);
					cell2.setBorder(Rectangle.NO_BORDER);
					cell2.addElement(parDate);

					tabHeader.addCell(cell2);
					document.add(tabHeader);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Font boldFontTitle = new Font(Font.FontFamily.HELVETICA, 20,
						Font.BOLD);
				LineSeparator ls = new LineSeparator();
				Paragraph p = new Paragraph("Catalogue de Produits",
						boldFontTitle);
				p.setIndentationLeft(200f);
				p.setSpacingBefore(30f);
				p.setSpacingAfter(5f);
				document.add(p);
				document.add(ls);
				Paragraph p2 = new Paragraph();
				p2.setSpacingAfter(30);
				document.add(p2);

				/*---------------------------------------------*/

				List<Product> allProduct = ProductManager.getAll();
				List<Unit> allUnit = UnitManager.getAll();
				List<Category> allCategory = CategoryManager.getAll();
				int i = 0;
				for (Product product : allProduct) {
					i++;
					PdfPTable table = new PdfPTable(2);
					table.setSpacingBefore(20f);
					creatTablePDF(table, product, document, allUnit,
							allCategory);
					document.add(table);
					if (i == 3) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(20);
						document.add(space);
					}
				}
				if (allProduct.size() < 4) {

					if (allProduct.size() == 1) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(392);
						document.add(space);
						pdfFooter(document);
					} else if (allProduct.size() == 2) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(212);
						document.add(space);
						pdfFooter(document);
					} else if (allProduct.size() == 3) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(28);
						document.add(space);
						pdfFooter(document);
					}
				} else {
					if ((allProduct.size() - 3) % 4 == 1) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(595);
						document.add(space);
						pdfFooter(document);

					} else if ((allProduct.size() - 3) % 4 == 2) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(415);
						document.add(space);
						pdfFooter(document);

					} else if ((allProduct.size() - 3) % 4 == 3) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(232);
						document.add(space);
						pdfFooter(document);

					} else if ((allProduct.size() - 3) % 4 == 0) {
						Paragraph space = new Paragraph();
						space.setSpacingBefore(48);
						document.add(space);
						pdfFooter(document);

					}
				}
				document.close();

			} catch (DocumentException | FileNotFoundException ex) {
				ex.printStackTrace();
			}

			try {
				Desktop.getDesktop().open(savedFile);
			} catch (Exception ex) {
				try {
					new CustomInfoBox(stage, BoxType.ERROR,
							"Impossible d'ouvrir ce fichier!", "Ok");

				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	private void pdfFooter(Document document) {
		try {
			PdfPTable tabFooter = new PdfPTable(3);
			LineSeparator ls1 = new LineSeparator();
			tabFooter.setWidthPercentage(100);

			document.add(ls1);

			PdfPCell cell1 = new PdfPCell(new Phrase("Great Fictiv Co"));
			cell1.setBorder(Rectangle.NO_BORDER);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabFooter.addCell(cell1);
			PdfPCell cell2 = new PdfPCell(new Phrase(
					"255 Cremazie Est, bureau 015 Montreal, Quebec, H2M 1M2"));

			cell2.setBorder(Rectangle.NO_BORDER);
			cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabFooter.addCell(cell2);
			PdfPCell cell3 = new PdfPCell(new Phrase(
					"Telephone: (514)-316-9202"));

			cell3.setBorder(Rectangle.NO_BORDER);
			cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabFooter.addCell(cell3);
			document.add(tabFooter);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private void creatTablePDF(PdfPTable table, Product product,
			Document document, List<Unit> unit, List<Category> category) {
		String descriptionCat = "";
		String descriptionUnit = "";

		for (Category cat : category) {
			if (cat.getId() == product.getCategory_id()) {
				descriptionCat = cat.getDescription();
			}
		}
		for (Unit temp : unit) {
			if (temp.getId() == product.getUnit_id()) {
				descriptionUnit = temp.getDescription();
			}
		}
		Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

		/*----- Nom Produit-------*/
		PdfPCell c2 = new PdfPCell(new Phrase("Nom de produit :"
				+ product.getName(), boldFont));
		c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		c2.setPadding(10);
		c2.setColspan(2);
		table.addCell(c2);

		/*----cell vide --------*/
		PdfPCell c1 = new PdfPCell(new Phrase(""));
		// table.addCell(c1);

		/*----cell Image --------*/
		try {
			Image image = null;
			if (product.getImg() == null)
				image = Image.getInstance(getClass().getResource(
						"/images/nopicture.jpg"));
			else
				image = Image.getInstance(product.getImg());

			image.scaleAbsoluteWidth(120);
			image.scaleAbsoluteHeight(120);

			PdfPCell imagecell = new PdfPCell(image);
			imagecell.setPadding(5);
			imagecell.setHorizontalAlignment(Element.ALIGN_CENTER);
			imagecell.setVerticalAlignment(Element.ALIGN_MIDDLE);

			table.addCell(imagecell);
		} catch (IOException | BadElementException e) {
			e.printStackTrace();
		}

		/*---- nouveau table --------*/
		PdfPTable tableInfoProduit = new PdfPTable(1);
		/*----- Nom Produit-------*/
		c1 = new PdfPCell(new Phrase("Categorie : " + descriptionCat, boldFont));
		c1.setBorder(Rectangle.NO_BORDER);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setPaddingLeft(10);
		c1.setPaddingTop(20);
		// c1.setPaddingBottom(5);
		tableInfoProduit.addCell(c1);
		/*----- Nom Produit-------*/
		c1 = new PdfPCell(new Phrase("Quantite : " + product.getQuantity(),
				boldFont));
		c1.setBorder(Rectangle.NO_BORDER);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setPaddingLeft(10);
		c1.setPaddingTop(10);
		// c1.setPaddingBottom(5);
		tableInfoProduit.addCell(c1);

		/*----- Nom Produit-------*/
		c1 = new PdfPCell(new Phrase("Prix : " + product.getSelling_price()
				+ " $", boldFont));
		c1.setBorder(Rectangle.NO_BORDER);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setPaddingLeft(10);
		c1.setPaddingTop(10);
		tableInfoProduit.addCell(c1);
		/*----- Nom Produit-------*/

		c1 = new PdfPCell(new Phrase("Unite : " + descriptionUnit, boldFont));
		c1.setBorder(Rectangle.NO_BORDER);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setPaddingLeft(10);
		c1.setPaddingTop(10);
		tableInfoProduit.addCell(c1);

		table.addCell(tableInfoProduit);

	}

}
