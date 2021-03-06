package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.managers.CommissionManager;
import com.devsolutions.camelus.managers.VendorManager;
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.StringUtils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CommissionsController implements Initializable {
	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Button addBtn;
	@FXML
	private Button modifyBtn;
	@FXML
	private Button deleteBtn;
	@FXML
	private ChoiceBox<String> chooseCommisionType;
	@FXML
	private TextField rateTxt;
	@FXML
	private TextField conditionTxt;
	@FXML
	private TableView<Commission> commissionTable;
	@FXML
	private TableColumn<Commission, Integer> typeCol;
	@FXML
	private TableColumn<Commission, Double> tauxCol;
	@FXML
	private TableColumn<Commission, Double> conditionCol;
	private ObservableList<Commission> commissionsOb;
	private SimpleBooleanProperty rateEntered;
	private SimpleBooleanProperty conditionEntered;
	private Commission commissionToModify;
	private int commissionToModifyIndex;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		rateEntered = new SimpleBooleanProperty();
		conditionEntered = new SimpleBooleanProperty();
		commissionTable.setPlaceholder(new Label("Pas de commissions"));
		lblClose.setOnMouseClicked(e -> {
			((Stage) lblClose.getScene().getWindow()).close();
		});

		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		tauxCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
		conditionCol.setCellValueFactory(new PropertyValueFactory<>(
				"mcondition"));
		typeCol.setCellFactory(column -> {
			return new TableCell<Commission, Integer>() {
				@Override
				protected void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						if (item == 0) {
							setText("pourcentage");
						} else {
							setText("fixe");
						}

					} else {
						setText("");
					}
				}
			};
		});
		commissionsOb = FXCollections.observableArrayList(CommissionManager
				.getAll());
		commissionTable.setItems(commissionsOb);
		chooseCommisionType.getItems().addAll("Pourcentage", "Fixe");
		chooseCommisionType.getSelectionModel().selectFirst();
		rateTxt.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if (StringUtils.validDecimal((newValue))) {
					rateEntered.set(true);
				} else {
					rateEntered.set(false);
				}

			}

		});
		conditionTxt.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if (StringUtils.validDecimal((newValue))) {
					conditionEntered.set(true);
				} else {
					conditionEntered.set(false);

				}

			}

		});
		rateEntered.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue.booleanValue() && conditionEntered.get()) {
					addBtn.setDisable(false);
				} else {
					addBtn.setDisable(true);
				}

			}
		});
		conditionEntered.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue.booleanValue() && rateEntered.get()) {
					addBtn.setDisable(false);
				} else {
					addBtn.setDisable(true);
				}

			}
		});
		addBtn.setOnAction(e -> {
			Stage stage = (Stage) addBtn.getScene().getWindow();
			double rate = Double.parseDouble(rateTxt.getText().trim());
			double mcondition = Double.parseDouble(conditionTxt.getText()
					.trim());
			int type = chooseCommisionType.getSelectionModel()
					.getSelectedIndex();
			boolean exist = false;
			if (addBtn.getText().equals("Confirmer")) {
				for (Commission commission : commissionsOb) {

					if (!commission.equals(commissionToModify)
							&& commission.getRate() == rate
							&& commission.getMcondition() == mcondition
							&& commission.getType() == type) {
						exist = true;
					}
				}
				if (!exist) {
					commissionToModify.setType(type);
					commissionToModify.setRate(rate);
					commissionToModify.setMcondition(mcondition);
					CommissionManager.update(commissionToModify);
					commissionsOb.set(commissionToModifyIndex,
							commissionToModify);
					addBtn.setText("Ajouter");
				} else {
					try {
						new CustomInfoBox(stage, BoxType.ERROR,
								"Cette combinaison existe deja!", "Ok");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} else {
				for (Commission commission : commissionsOb) {

					if (commission.getRate() == rate
							&& commission.getMcondition() == mcondition
							&& commission.getType() == type) {
						exist = true;
					}
				}
				if (!exist) {
					Commission c = new Commission();
					c.setType(type);
					c.setRate(rate);
					c.setMcondition(mcondition);
					CommissionManager.add(c);
					commissionsOb.add(c);
				} else {
					try {
						new CustomInfoBox(stage, BoxType.ERROR,
								"Cette combinaison existe deja!", "Ok");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

			reinitialise();
		});
		commissionTable.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Commission>() {

					@Override
					public void changed(
							ObservableValue<? extends Commission> observable,
							Commission oldValue, Commission newValue) {
						if (newValue != null) {
							modifyBtn.setDisable(false);
							deleteBtn.setDisable(false);

						} else {
							modifyBtn.setDisable(true);
							deleteBtn.setDisable(true);
						}

					}

				});
		modifyBtn.setOnAction(e -> {
			commissionToModifyIndex = commissionTable.getSelectionModel()
					.getSelectedIndex();
			commissionToModify = commissionTable.getSelectionModel()
					.getSelectedItem();
			rateTxt.setText("" + commissionToModify.getRate());
			conditionTxt.setText("" + commissionToModify.getMcondition());
			chooseCommisionType.getSelectionModel().select(
					commissionToModify.getType());
			addBtn.setText("Confirmer");
		});
		deleteBtn
				.setOnAction(e -> {

					Commission c = commissionTable.getSelectionModel()
							.getSelectedItem();
					if (VendorManager.getByCommission(c.getId()).isEmpty()) {
						CommissionManager.delete(c.getId());
						commissionsOb.remove(c);
						commissionTable.getSelectionModel().clearSelection();
						reinitialise();
					} else {
						try {
							new CustomInfoBox(
									(Stage) deleteBtn.getScene().getWindow(),
									BoxType.INFORMATION,
									"Cette commission a été déjà attribuer à un vendeur",
									"Ok");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}

				});
	}

	private void reinitialise() {
		rateTxt.clear();
		conditionTxt.clear();
		chooseCommisionType.getSelectionModel().selectFirst();
	}

}
