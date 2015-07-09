package com.devsolutions.camelus.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.VendorReport;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.ClientManager;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.OrderLineManager;
import com.devsolutions.camelus.managers.OrderManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.utils.Choice;
import com.devsolutions.camelus.utils.CustomInfoBox;
import com.devsolutions.camelus.utils.StringUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ShowVendorsController implements Initializable {
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
	private TableView<Vendor> vendorTableView;
	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button showButton;
	@FXML
	private Button commissionBtn;
	@FXML
	private Button ordersBtn;

	@FXML
	private TextField searchField;

	@FXML
	private ChoiceBox<Choice> monthComboBox;
	@FXML
	private ChoiceBox<Choice> yearComboBox;

	@FXML
	private TableColumn<Vendor, String> vendorNameCol;
	@FXML
	private TableColumn<Vendor, String> vendorIdCol;
	@FXML
	private TableColumn<Vendor, String> vendorLoginCol;

	private List<Vendor> vendorsList;
	private ObservableList<Vendor> vendorsObservableList;
	private SortedList<Vendor> sortedData;
	private List<VendorReport> currentCommissionTVList;

	private MainWindowController mainWindowController;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		monthComboBox.setDisable(true);
		yearComboBox.setDisable(true);
		commissionBtn.setDisable(true);
		gridRowTwo.setVisible(false);
		gridRowOne.setVisible(false);
		vendorsList = VendorManager.getAll();
		vendorsObservableList = FXCollections.observableArrayList();
		vendorsObservableList.addAll(vendorsList);
		if (vendorsObservableList.size() == 0) {
			noDataToShow();
		} else {
			showTableView();
		}

		initTableView();

		FilteredList<Vendor> filteredData = new FilteredList<>(
				vendorsObservableList, p -> true);

		searchField.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					filteredData.setPredicate(vendor -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}

						String lowerCaseFilter = newValue.toLowerCase();
						String id = vendor.getId() + "";

						if (vendor.getFname().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (vendor.getLname().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (vendor.getLogin().toLowerCase()
								.contains(lowerCaseFilter)) {
							return true;
						} else if (id.contains(lowerCaseFilter)) {
							return true;
						}

						return false;
					});
				});

		sortedData = new SortedList<>(filteredData);

		sortedData.comparatorProperty().bind(
				vendorTableView.comparatorProperty());

		vendorTableView.setItems(sortedData);

		resetComboBox();
		initMonthComboBox();

		currentCommissionTVList = new ArrayList<VendorReport>();

		addButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(
						"../views/addupdatevendor.fxml"));

				Stage newStage = new Stage();
				Scene scene;

				scene = new Scene(loader.load());

				newStage.setScene(scene);

				AddUpdateVendorController controller = loader
						.<AddUpdateVendorController> getController();
				controller.initData(this, "Ajouter", null, -1);
				controller.setTitleWindow("Aouter vendeur");
				newStage.initStyle(StageStyle.UNDECORATED);
				newStage.initModality(Modality.APPLICATION_MODAL);
				newStage.initOwner(motherGrid.getScene().getWindow());
				newStage.show();
				Stage parentStage = (Stage) motherGrid.getScene().getWindow();
				centerStage(parentStage, newStage, 22);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		deleteButton.setOnAction(e -> {
			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			if (vendor != null) {
				VendorManager.delete(vendor.getId());
				removeFromTableView(vendor);
			}

		});

		editButton.setOnAction(e -> {

			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			int index = vendorTableView.getSelectionModel().getSelectedIndex();
			if (vendor != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"../views/addupdatevendor.fxml"));

					Stage newStage = new Stage();
					Scene scene;

					scene = new Scene(loader.load());
					newStage.setScene(scene);

					AddUpdateVendorController controller = loader
							.<AddUpdateVendorController> getController();
					controller.initData(this, "Modifier", vendor, index);
					controller.setTitleWindow("Modifier vendeur");
					newStage.initStyle(StageStyle.UNDECORATED);
					newStage.initModality(Modality.APPLICATION_MODAL);
					newStage.initOwner(motherGrid.getScene().getWindow());
					newStage.show();
					Stage parentStage = (Stage) motherGrid.getScene().getWindow();
					centerStage(parentStage, newStage, 22);

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		

		
		showButton.setOnAction(e -> {
			Vendor vendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			if (vendor != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(
							"../views/showvendordetails.fxml"));

					Stage newStage = new Stage();
					Scene scene;

					scene = new Scene(loader.load());
					newStage.setScene(scene);

					ShowVendorDetailsController controller = loader
							.<ShowVendorDetailsController> getController();
					controller.initData(vendor);

					newStage.initStyle(StageStyle.UNDECORATED);
					newStage.initModality(Modality.APPLICATION_MODAL);
					newStage.initOwner(motherGrid.getScene().getWindow());
					newStage.show();
					Stage parentStage = (Stage) motherGrid.getScene().getWindow();
					centerStage(parentStage, newStage, 22);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		commissionBtn.setOnAction(e -> {
			
			int selectedYearId = 0;
			if (!yearComboBoxIsEmpty())
				selectedYearId = yearComboBox
						.getSelectionModel().getSelectedItem()
						.getId();
			int selectedMonthId = monthComboBox
					.getSelectionModel().getSelectedItem()
					.getId();
			
			if (selectedYearId>0 && selectedMonthId>0)
			{
				comissionBtnAction();
				creatPDF();
			}else
			{
				try {
					Stage parentStage = (Stage) motherGrid.getScene().getWindow();
					CustomInfoBox customDialogBox = new CustomInfoBox(parentStage,
							"Il faut choisir un mois et une année pour générer un rapport.", "Ok", "#303030");
					customDialogBox.btn
							.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									stage = (Stage) customDialogBox.btn
											.getScene().getWindow();
									stage.close();
								}
							});
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}

		});

		ordersBtn.setOnAction(e -> {
			Vendor selectedVendor = vendorTableView.getSelectionModel()
					.getSelectedItem();
			mainWindowController.switchScene("showorders", selectedVendor);
		});

		vendorTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						deleteButton.setDisable(false);
						editButton.setDisable(false);
						showButton.setDisable(false);
						ordersBtn.setDisable(false);
						monthComboBox.setDisable(false);
						yearComboBox.setDisable(false);
						commissionBtn.setDisable(false);
						initComboBox();

					} else {
						deleteButton.setDisable(true);
						editButton.setDisable(true);
						showButton.setDisable(true);
						ordersBtn.setDisable(true);
						monthComboBox.setDisable(true);
						yearComboBox.setDisable(true);
						commissionBtn.setDisable(true);
						resetComboBox();
					}
					monthComboBox.getSelectionModel().select(0);
				});

		monthComboBox
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(obs, oldSelection, newSelection) -> {

							int selectedYearId = 0;
							if (!yearComboBoxIsEmpty())
								selectedYearId = yearComboBox
										.getSelectionModel().getSelectedItem()
										.getId();

							if (newSelection != null
									&& newSelection.getId() > 0
									&& selectedYearId > 0) {
								//commissionBtn.setDisable(false);
							} else {
								//commissionBtn.setDisable(true);
							}
						});

		yearComboBox
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(obs, oldSelection, newSelection) -> {
							int selectedMonthId = monthComboBox
									.getSelectionModel().getSelectedItem()
									.getId();

							if (newSelection != null && selectedMonthId > 0
									&& newSelection.getId() > 0) {
								//commissionBtn.setDisable(false);
							} else {
								//commissionBtn.setDisable(true);
							}
							initMonthComboBox();
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

	private void creatPDF() {
		Document document = new Document();
		int selectedMonth = monthComboBox.getSelectionModel().getSelectedItem()
				.getId();
		int selectedYear = yearComboBox.getSelectionModel().getSelectedItem()
				.getId();
		Vendor vendor = vendorTableView.getSelectionModel().getSelectedItem();

		stage = (Stage) commissionBtn.getScene().getWindow();

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Exporter le rapport de commission");
		String defaultFileName = "Rapport-de-commission_" + vendor.getFname()
				+ "-" + vendor.getLname() + "(" + selectedMonth + "-"
				+ selectedYear + ").pdf";
		fileChooser.setInitialFileName(defaultFileName);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"PDF Files", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);
		File savedFile = fileChooser.showSaveDialog(stage);

		if (savedFile != null) {

			try {
				PdfWriter.getInstance(document,
						new FileOutputStream(savedFile.getAbsoluteFile()));

				document.setPageSize(PageSize.A4);
				document.setMargins(0, 0, 30, 0);
				document.open();
				Font boldFont = new Font(Font.FontFamily.HELVETICA, 18,
						Font.BOLD);
				Paragraph p1 = new Paragraph();
				p1.setIndentationLeft(50f);
				p1.setIndentationRight(50f);
				p1.setSpacingAfter(30f);

				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);

				p1.add(new Phrase(
						"No       :   "
								+ vendor.getId()
								+ "                                                    Date: "
								+ StringUtils.formateDate(day, month + 1, year),
						boldFont));
				document.add(p1);

				Paragraph p = new Paragraph("Nom   :   " + vendor.getFname()
						+ " " + vendor.getLname(), boldFont);
				p.setIndentationLeft(50f);

				p.setSpacingAfter(50f);
				document.add(p);

				Font boldFontTitle = new Font(Font.FontFamily.HELVETICA, 24,
						Font.BOLD);

				Paragraph p2 = new Paragraph("Rapport de commission",
						boldFontTitle);
				p2.setAlignment(Element.ALIGN_CENTER);

				p2.setSpacingAfter(30f);
				document.add(p2);

				Paragraph p3 = new Paragraph("("
						+ StringUtils.monthName(selectedMonth) + " - "
						+ selectedYear + ")", boldFontTitle);
				p3.setAlignment(Element.ALIGN_CENTER);

				p3.setSpacingAfter(50f);
				document.add(p3);

				PdfPTable table = new PdfPTable(4);

				double totaSales = 0;
				double totalCommission = 0;

				for (VendorReport commissionTV : currentCommissionTVList) {
					totaSales += commissionTV.getTotal_sale();
					totalCommission += commissionTV.getTotal_commission();
				}
				creatTablePDF(table, totaSales, totalCommission, document);
				document.add(table);

				document.close();

			} catch (DocumentException | FileNotFoundException ex) {
				ex.printStackTrace();
			}

			String[] params = { "cmd", "/c", savedFile.getAbsolutePath() };
			try {
				Runtime.getRuntime().exec(params);
			} catch (Exception ex) {
				System.out.println("failed");
			}
		} else {
			System.out.println("no file");
		}
	}

	private void creatTablePDF(PdfPTable table, double totaSales,
			double totalCommission, Document document) {

		Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

		PdfPTable tableNoItems = new PdfPTable(1);
		PdfPCell c1 = new PdfPCell(new Phrase(
				"Aucune vente n'a ï¿½tï¿½ effectuï¿½e durant ce mois.",
				boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setFixedHeight(45f);
		tableNoItems.addCell(c1);

		c1 = new PdfPCell(new Phrase("No Client", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Client", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Vente ($)", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Commission ($)", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		for (VendorReport commissionTV : currentCommissionTVList) {
			c1 = new PdfPCell(new Phrase("" + commissionTV.getClient_id()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase(commissionTV.getEntreprise_name()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("" + commissionTV.getTotal_sale()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase(""
					+ commissionTV.getTotal_commission()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
		}

		if (currentCommissionTVList.size() == 0) {
			c1 = new PdfPCell(new Phrase(
					"Aucune vente n'a ï¿½tï¿½ effectuï¿½e durant ce mois.",
					boldFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c1.setColspan(4);
			c1.setFixedHeight(45f);
			tableNoItems.addCell(c1);
			table.addCell(c1);
		}

		c1 = new PdfPCell(new Phrase("Total ", boldFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setColspan(2);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("" + totaSales + "$"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("" + totalCommission + " $"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

	}

	private void comissionBtnAction() {
		Vendor vendor = vendorTableView.getSelectionModel().getSelectedItem();

		int selectedMonth = monthComboBox.getSelectionModel().getSelectedItem()
				.getId();
		int selectedYear = yearComboBox.getSelectionModel().getSelectedItem()
				.getId();

		List<OrderTV> orders = OrderManager.getByVendorId(vendor.getId());
		List<OrderTV> ordersNeeded = new ArrayList<OrderTV>();

		for (OrderTV orderTV : orders) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(orderTV.getOrdered_at());
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);

			if (month + 1 == selectedMonth && year == selectedYear) {
				ordersNeeded.add(orderTV);
			}

		}

		List<OrderLineTV> orderLinesTVNeeded = new ArrayList<OrderLineTV>();
		List<VendorReport> commissionTVList = new ArrayList<VendorReport>();
		Commission commission = CommissionManager.getById(vendor
				.getCommission_id());
		for (OrderTV orderTV : ordersNeeded) {
			List<OrderLineTV> orderLinesTV = OrderLineManager
					.getByOrderId(orderTV.getId());
			for (OrderLineTV orderLineTV : orderLinesTV) {
				orderLinesTVNeeded.add(orderLineTV);
			}

			double totalSale = 0;

			double commissionAmount = 0;
			for (OrderLineTV orderLineTV : orderLinesTVNeeded) {
				totalSale += orderLineTV.getTotal();
			}

			if (totalSale >= commission.getMcondition()) {

				if (commission.getType() == 0) {
					commissionAmount = (totalSale * commission.getRate()) / 100;
				} else
					commissionAmount = commission.getRate();
			}

			VendorReport commissionTV = new VendorReport();
			commissionTV.setClient_id(orderTV.getClient_id());
			commissionTV.setEntreprise_name(orderTV.getEnterprise_name());
			commissionTV.setTotal_sale(totalSale);
			commissionTV.setTotal_commission(commissionAmount);

			commissionTVList.add(commissionTV);
		}

		currentCommissionTVList.clear();
		currentCommissionTVList.addAll(commissionTVList);
	}

	private void resetComboBox() {
		yearComboBox.getItems().clear();
		ObservableList<Choice> yearObservableList = FXCollections
				.observableArrayList();
		yearObservableList.add(new Choice(0, "Année"));
		yearComboBox.setItems(yearObservableList);
		yearComboBox.getSelectionModel().select(0);
	}

	private boolean yearComboBoxIsEmpty() {
		boolean yes = true;
		if (yearComboBox.getItems().size() > 0)
			yes = false;

		return yes;
	}

	private boolean monthComboBoxIsEmpty() {
		boolean yes = true;
		if (monthComboBox.getItems().size() > 0)
			yes = false;

		return yes;
	}

	private void initMonthComboBox() {
		int selectedMonthId = 0;
		if (!monthComboBoxIsEmpty())
			selectedMonthId = monthComboBox.getSelectionModel()
					.getSelectedItem().getId();
		monthComboBox.getItems().clear();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int selectedYearId = 0;
		if (yearComboBox.getItems().size() > 0)
			selectedYearId = yearComboBox.getSelectionModel().getSelectedItem()
					.getId();

		ObservableList<Choice> monthObservableList = FXCollections
				.observableArrayList();
		monthObservableList.add(new Choice(0, "Mois"));
		for (int i = 1; i <= 12; i++) {
			if (selectedYearId == year && selectedYearId != 0) {
				if (i <= month + 1)
					monthObservableList.add(new Choice(i, ""
							+ StringUtils.monthName(i)));
			} else {
				monthObservableList.add(new Choice(i, ""
						+ StringUtils.monthName(i)));
			}
		}
		monthComboBox.setItems(monthObservableList);
		if (monthComboBox.getItems().size() > 0
				&& selectedMonthId > monthComboBox.getItems()
						.get(monthComboBox.getItems().size() - 1).getId()) {
			monthComboBox.getSelectionModel().select(0);
		} else {
			monthComboBox.getSelectionModel().select(selectedMonthId);
		}

	}

	private void initComboBox() {
		Vendor vendor = vendorTableView.getSelectionModel().getSelectedItem();
		List<OrderTV> orders = OrderManager.getByVendorId(vendor.getId());

		yearComboBox.getItems().clear();
		ObservableList<Choice> yearsObservableList = FXCollections
				.observableArrayList();
		yearsObservableList.add(new Choice(0, "Année"));
		for (OrderTV orderTV : orders) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(orderTV.getOrdered_at());
			int year = cal.get(Calendar.YEAR);

			if (!exist(yearsObservableList, year))
				yearsObservableList.add(new Choice(year, "" + year));
		}

		yearComboBox.setItems(yearsObservableList);
		yearComboBox.getSelectionModel().select(0);
	}

	private boolean exist(ObservableList<Choice> years, int year) {
		boolean exist = false;

		for (Choice choice : years) {
			if (choice.getId() == year)
				exist = true;
		}

		return exist;
	}

	public void initTableView() {
		vendorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		vendorNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		vendorLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
	}

	public void initData(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
	}

	public void addToTableView(Vendor vendor) {
		vendorsObservableList.add(vendor);
	}

	public void removeFromTableView(Vendor vendor) {
		vendorsObservableList.remove(vendor);
	}

	public void updateTableView(int index, Vendor vendor) {
		vendorsObservableList.set(index, vendor);
	}

	public TableView<Vendor> getTable() {
		TableView<Vendor> table = vendorTableView;
		return table;
	}
	
	private void centerStage(Stage parentStage, Stage childStage, int y) {
		childStage.setX(parentStage.getX() + parentStage.getWidth() / 2
				- childStage.getWidth() / 2);
		childStage
				.setY((parentStage.getY() + parentStage.getHeight() / 2 - childStage
						.getHeight() / 2) + y);
	}
}
