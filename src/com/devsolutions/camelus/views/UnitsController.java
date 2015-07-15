package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.ProductManager;
import com.devsolutions.camelus.managers.UnitManager;
import com.devsolutions.camelus.utils.FXUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class UnitsController implements Initializable {
	@FXML
	private GridPane titleBar;
	@FXML
	private ListView<Unit> unitslist;
	@FXML
	private ObservableList<Unit> unitsOb;
	@FXML
	private Button addBtn;
	@FXML
	private Button removeBtn;
	@FXML
	private Label lblClose;
	@FXML
	private Label msgtxt;
	private boolean isNew;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		unitslist.setPlaceholder(new Label("Pas de unites"));
		unitsOb = FXCollections.observableArrayList(UnitManager.getAll());
		if (!unitsOb.isEmpty()) {
			unitsOb.remove(0);
		}
		unitslist.setItems(unitsOb);
		unitslist.setEditable(true);
		lblClose.setOnMouseClicked(e -> {
			((Stage) lblClose.getScene().getWindow()).close();
		});
		unitslist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		final Timeline animation = new Timeline(new KeyFrame(
				Duration.seconds(.1), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						unitslist.edit(unitslist.getSelectionModel()
								.getSelectedIndex());
					}
				}));
		animation.setCycleCount(1);

		addBtn.setOnAction(e -> {
			addBtn.setDisable(true);
			unitslist.getItems().add(new Unit(""));
			unitslist.getSelectionModel().selectLast();
			animation.play();
			msgtxt.setText("Saisir une unité puis taper sur entrée.");
		});
		unitslist.setCellFactory(TextFieldListCell
				.forListView(new StringConverter<Unit>() {

					@Override
					public String toString(Unit object) {

						return object.toString();
					}

					@Override
					public Unit fromString(String string) {

						return new Unit(string);
					}

				}));
		unitslist.setOnEditStart(new EventHandler<ListView.EditEvent<Unit>>() {

			@Override
			public void handle(EditEvent<Unit> event) {
				if (unitsOb.get(event.getIndex()).getId() == 0) {
					isNew = true;
				} else {
					isNew = false;
				}

			}
		});
		unitslist.setOnEditCommit(new EventHandler<ListView.EditEvent<Unit>>() {

			@Override
			public void handle(EditEvent<Unit> event) {
				addBtn.setDisable(false);
				Unit unit = event.getNewValue();
				int index = event.getIndex();
				Unit unit2 = unitsOb.get(index);
				if (unit.getDescription().length() > 20) {
					msgtxt.setText("20 caractères max");
				} else {
					if (!unit.getDescription().isEmpty()) {
						if (!FXUtils.customcontains(unitsOb,
								unit.getDescription())) {
							if (isNew) {
								UnitManager.add(unit);

								unitsOb.set(index, unit);
							} else {

								unit2.setDescription(unit.getDescription());
								unitsOb.set(index, unit2);
								UnitManager.update(unit2);

							}
							msgtxt.setText("");
						} else {
							msgtxt.setText("Cette unite existe deja!");
						}
					} else {

						if (!isNew) {
							if (ProductManager.getByUnit(unit2.getId())
									.isEmpty()) {
								unitsOb.remove(index);
								UnitManager.delete(unit2.getId());
								msgtxt.setText("");
							} else {
								msgtxt.setText("Cette unite est lié avec un produit!");

							}
						}
					}
				}
			}
		});
		removeBtn.setOnAction(e -> {
			Unit unit = unitslist.getSelectionModel().getSelectedItem();
			if (ProductManager.getByUnit(unit.getId()).isEmpty()) {
				unitsOb.remove(unit);
				UnitManager.delete(unit.getId());
				msgtxt.setText("");
			} else {
				msgtxt.setText("Cette unite est lié avec un produit!");
			}
		});
		unitslist.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Unit>() {

					@Override
					public void changed(
							ObservableValue<? extends Unit> observable,
							Unit oldValue, Unit newValue) {
						if (newValue != null) {
							removeBtn.setDisable(false);
						} else {
							removeBtn.setDisable(true);
						}

					}

				});
	}

}
