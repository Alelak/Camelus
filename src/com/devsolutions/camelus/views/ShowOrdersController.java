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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.managers.OrderLineManager;
import com.devsolutions.camelus.managers.OrderManager;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.FileUtils;
import com.devsolutions.camelus.utils.StringUtils;
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

public class ShowOrdersController implements Initializable {

	@FXML
	private TableView<OrderTV> orderTableView;

	@FXML
	private Button takeOrderBtn;
	@FXML
	private Button cancelOrderBtn;
	@FXML
	private Button showOrderBtn;
	@FXML
	private Button btnRefresh;
	@FXML
	private Button pdfBtn;

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
	private Label message2;
	@FXML
	private RowConstraints rowTwo;
	@FXML
	private GridPane gridRowTwo;

	@FXML
	private TextField searchField;

	@FXML
	private TableColumn<OrderTV, String> orderIdCol;

	@FXML
	private TableColumn<OrderTV, String> vendorNameCol;
	@FXML
	private TableColumn<OrderTV, String> clientNameCol;
	@FXML
	private TableColumn<OrderTV, Date> orderedAtCol;
	@FXML
	private TableColumn<OrderTV, String> statusCol;

	private ObservableList<OrderTV> ordersObservableList;
	private SortedList<OrderTV> sortedData;

	private List<OrderLineTV> orderLinesList;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(false);

		if (Session.vendor != null) {
			ordersObservableList = FXCollections
					.observableArrayList(OrderManager
							.getByVendorId(Session.vendor.getId()));
		} else {
			ordersObservableList = FXCollections
					.observableArrayList(OrderManager.getAllTV());
			message2.setText("Effectuer une commande.");
		}

		if (ordersObservableList.size() == 0) {

			noDataToShow();
		} else {
			showTableView();
		}
		initTableView();

		FilteredList<OrderTV> filteredData = new FilteredList<>(
				ordersObservableList, p -> true);

		searchField.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(orderTV -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}

						String lowerCaseFilter = newValue.toLowerCase();
						String idOrder = orderTV.getId() + "";
						String fullName = (orderTV.getFname() + " " + orderTV
								.getLname()).toLowerCase();
						if (orderTV.getEnterprise_name().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (fullName.contains(lowerCaseFilter)) {
							return true;
						} else if (idOrder.contains(lowerCaseFilter)) {
							return true;
						}
						return false;
					});
				});
		sortedData = new SortedList<>(filteredData);

		sortedData.comparatorProperty().bind(
				orderTableView.comparatorProperty());

		orderTableView.setItems(sortedData);

		btnRefresh.setOnAction(e -> {
			ordersObservableList.clear();
			if (Session.vendor != null) {

				ordersObservableList.addAll(OrderManager
						.getByVendorId(Session.vendor.getId()));
			} else {
				ordersObservableList.addAll(OrderManager.getAllTV());

			}
		});

		showOrderBtn.setOnAction(e -> {
			OrderTV orderTV = orderTableView.getSelectionModel()
					.getSelectedItem();
			if (orderTV != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"ShowOrderDetails.fxml"));

					Stage newStage = new Stage();
					Scene scene;

					scene = new Scene(loader.load());
					scene.getStylesheets().addAll(
							getClass().getResource("childWindow.css")
									.toExternalForm(),
							getClass().getResource("Content.css")
									.toExternalForm());
					newStage.setScene(scene);

					ShowOrderDetailsController controller = loader
							.<ShowOrderDetailsController> getController();
					controller.initData(orderTV);

					newStage.initStyle(StageStyle.UNDECORATED);
					newStage.initModality(Modality.APPLICATION_MODAL);
					newStage.initOwner(motherGrid.getScene().getWindow());

					newStage.show();

					FXUtils.centerStage((Stage) motherGrid.getScene()
							.getWindow(), newStage, 22);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		takeOrderBtn
				.setOnAction(e -> {
					try {
						FXMLLoader loader = new FXMLLoader(getClass()
								.getResource("TakeOrderView.fxml"));

						Stage newStage = new Stage();
						Scene scene;

						scene = new Scene(loader.load());
						scene.getStylesheets().addAll(
								getClass().getResource("childWindow.css")
										.toExternalForm(),
								getClass().getResource("Content.css")
										.toExternalForm());
						newStage.setScene(scene);

						TakeOrderController controller = loader
								.<TakeOrderController> getController();
						controller.initData(this);

						newStage.initStyle(StageStyle.UNDECORATED);
						newStage.initModality(Modality.APPLICATION_MODAL);
						newStage.initOwner(motherGrid.getScene().getWindow());
						newStage.show();
						stage = (Stage) pdfBtn.getScene().getWindow();
						FXUtils.centerStage(stage, newStage, 22);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});

		pdfBtn.setOnAction(e -> {
			creatPDF();
		});

		cancelOrderBtn.setOnAction(e -> {
			try {
				CustomDialogBox dialogBox = new CustomDialogBox(
						(Stage) cancelOrderBtn.getScene().getWindow(),
						"Voulez vous vraiment annuler cette commande?", "Oui",
						"Non");
				dialogBox.positiveButton
						.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								OrderTV orderTV = orderTableView
										.getSelectionModel().getSelectedItem();
								OrderManager.cancel(orderTV.getId());
								orderTV.setStatus(1);
								int index = orderTableView.getSelectionModel()
										.getSelectedIndex();
								updateTableView(index, orderTV);
								cancelOrderBtn.setDisable(true);
								dialogBox.stage.close();
							}

						});

			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});

		orderTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						showOrderBtn.setDisable(false);
						pdfBtn.setDisable(false);
						if (newSelection.getStatus() != 1)
							cancelOrderBtn.setDisable(false);
						else
							cancelOrderBtn.setDisable(true);
					} else {
						showOrderBtn.setDisable(true);
						pdfBtn.setDisable(true);
						cancelOrderBtn.setDisable(true);
					}
				});
	}

	private void creatPDF() {
		Document document = new Document();
		OrderTV orderTV = orderTableView.getSelectionModel().getSelectedItem();
		Client client = ClientManager.getById(orderTV.getClient_id());
		stage = (Stage) pdfBtn.getScene().getWindow();
		String defaultFileName = "Commande-No " + orderTV.getId() + "_"
				+ orderTV.getEnterprise_name() + ".pdf";

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Exporter la Commande");
		fileChooser.setInitialFileName(defaultFileName);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"PDF Files", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);

		File savedFile = fileChooser.showSaveDialog(stage);

		if (savedFile != null) {
			if (!FileUtils.isFileLockedReadOnly(savedFile)) {

				try {
					PdfWriter.getInstance(document, new FileOutputStream(
							savedFile.getAbsolutePath()));
					document.setPageSize(PageSize.A4);
					document.setMargins(0, 0, 10, 0);
					document.open();

					PdfPTable table = new PdfPTable(6);

					Font boldFont = new Font(Font.FontFamily.HELVETICA, 12,
							Font.BOLD);

					LineSeparator ls = new LineSeparator();

					Paragraph p1 = new Paragraph();
					p1.add(ls);
					p1.setSpacingAfter(25f);
					p1.setSpacingBefore(25f);

					try {
						Font boldFontHeaderFields = new Font(
								Font.FontFamily.HELVETICA, 12, Font.BOLD);

						Image image = Image.getInstance("src/images/gfc.png");

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

						Paragraph p = new Paragraph(new Phrase(
								"Numero de commande : " + orderTV.getId()
										+ "\n", boldFontHeaderFields));
						p.add(new Phrase("Numero du client          : "
								+ orderTV.getClient_id() + "\n",
								boldFontHeaderFields));
						p.add(new Phrase("Numero du vendeur     : "
								+ orderTV.getAssociated_vendor(),
								boldFontHeaderFields));

						p.setIndentationLeft(50f);
						cell2.addElement(p);
						cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell2.setBorder(Rectangle.NO_BORDER);
						tabHeader.addCell(cell2);
						document.add(tabHeader);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					Font boldFontTitle = new Font(Font.FontFamily.HELVETICA,
							20, Font.BOLD);
					Paragraph p = new Paragraph("Bon de commande",
							boldFontTitle);
					p.setIndentationLeft(200f);
					document.add(p);

					document.add(p1);

					Paragraph p2 = new Paragraph();
					p2.setIndentationLeft(20f);
					p2.add(new Phrase("Entreprise : "));
					p2.add(new Phrase(client.getEnterprise_name() + "\n",
							boldFont));

					p2.add(new Phrase("Adresse      : "));
					p2.add(new Phrase(client.getAddress(), boldFont));

					p2.setSpacingBefore(10f);
					p2.setSpacingAfter(10f);

					document.add(p2);

					Paragraph p3 = new Paragraph();
					p3.setIndentationLeft(20f);
					p3.add(new Phrase("Email : "));
					p3.add(new Phrase(client.getContact_email() + "       ",
							boldFont));

					p3.add(new Phrase("Tel : "));
					p3.add(new Phrase(client.getContact_tel() + "       ",
							boldFont));

					p3.add(new Phrase("Date : "));
					p3.add(new Phrase(StringUtils.formatDate(orderTV
							.getOrdered_at()), boldFont));

					p3.setSpacingBefore(10f);
					p3.setSpacingAfter(10f);

					document.add(p3);

					float[] columnWidths = new float[] { 60f, 60f, 50f, 60f,
							40f, 40f };
					table.setWidths(columnWidths);

					table.setHeaderRows(1);

					creatTablePDF(table, orderTV);

					Font boldFontDetails = new Font(Font.FontFamily.HELVETICA,
							18, Font.BOLD);
					Paragraph p4 = new Paragraph(new Phrase("Details : ",
							boldFontDetails));
					p4.setIndentationLeft(20f);
					p4.setSpacingAfter(50f);
					document.add(p4);

					Paragraph p5 = new Paragraph();
					p5.setSpacingAfter(20f);
					p5.add(table);
					document.add(p5);

					Font boldFontTotal = new Font(Font.FontFamily.HELVETICA,
							14, Font.BOLD);

					double total = 0;
					for (OrderLineTV orderLineTV : orderLinesList) {

						total += orderLineTV.getTotal();
					}

					Paragraph p6 = new Paragraph();
					p6.setAlignment(Element.ALIGN_RIGHT);
					p6.setIndentationRight(60f);
					p6.setSpacingBefore(10f);
					p6.add(new Phrase("Total de la commande : " + total + " $",
							boldFontTotal));
					document.add(p6);

					Paragraph p7 = new Paragraph();
					p7.setIndentationLeft(60f);
					p7.setSpacingBefore(50f);
					p7.setSpacingAfter(30f);
					p7.add(new Phrase(
							"Le : "
									+ StringUtils.formatDate(orderTV
											.getOrdered_at())
									+ "                                                  Signature : ",
							boldFontTotal));
					document.add(p7);

					document.close();

				} catch (DocumentException | FileNotFoundException ex) {
					ex.printStackTrace();
				}
				try {
					Desktop.getDesktop().open(savedFile);
				} catch (Exception ex) {
					try {
						new CustomInfoBox(stage,
								"Impossible d'ouvrir ce fichier!", "Ok");
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			} else {
				try {
					new CustomInfoBox(
							stage,
							"Ce fichier est deja ouvert, veuillez le fermer puis reessayer de nouveau.",
							"Ok");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	private void creatTablePDF(PdfPTable table, OrderTV orderTV) {
		orderLinesList = OrderLineManager.getByOrderId(orderTV.getId());
		Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

		PdfPCell c1 = new PdfPCell(new Phrase("UPC", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Produit", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Prix ($)", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Prix ajuste ($)", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Quantite", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Total ($)", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		for (OrderLineTV orderLineTV : orderLinesList) {
			c1 = new PdfPCell(new Phrase("" + orderLineTV.getUpc()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase(orderLineTV.getName()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("" + orderLineTV.getSelling_price()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("" + orderLineTV.getModified_price()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("" + orderLineTV.getQuantity()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("" + orderLineTV.getTotal()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
		}

	}

	private void initTableView() {

		orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		if (Session.admin != null) {
			vendorNameCol.setCellValueFactory(new PropertyValueFactory<>(
					"fullname"));
		} else {
			orderTableView.getColumns().remove(vendorNameCol);
		}

		clientNameCol.setCellValueFactory(new PropertyValueFactory<>(
				"enterprise_name"));
		orderedAtCol.setCellValueFactory(new PropertyValueFactory<>(
				"ordered_at"));
		orderedAtCol.setCellFactory(column -> {
			return new TableCell<OrderTV, Date>() {
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(StringUtils.formatDate(item));
					} else {
						setText("");
					}
				}
			};
		});
		statusCol
				.setCellValueFactory(new PropertyValueFactory<>("canceledText"));
	}

	public void addToTableView(OrderTV orderTV) {
		ordersObservableList.add(orderTV);
	}

	public void updateTableView(int index, OrderTV orderTV) {
		ordersObservableList.set(index, orderTV);
	}

	public void filterByVendor(Vendor selectedVendor) {
		searchField.setText(selectedVendor.getFname() + " "
				+ selectedVendor.getLname());
	}

	public void filterByClient(Client selectedClient) {
		searchField.setText(selectedClient.getEnterprise_name());
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

}