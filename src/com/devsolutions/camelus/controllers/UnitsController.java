package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.UnitManager;

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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class UnitsController implements Initializable {
	private double initialX;
	private double initialY;
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
	private boolean isNew;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addDraggableNode(titleBar);
		unitslist.setPlaceholder(new Label("Pas de unites"));
		unitsOb = FXCollections.observableArrayList(UnitManager.getAll());
		unitsOb.remove(0);
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
			unitslist.getItems().add(new Unit());
			unitslist.getSelectionModel().selectLast();
			animation.play();
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
				Unit unit = event.getNewValue();
				int index = event.getIndex();
				Unit unit2 = unitsOb.get(index);
				if (!event.getNewValue().getDescription().isEmpty()) {
					if (isNew) {
						UnitManager.add(unit);

						unitsOb.set(index, unit);
					} else {

						unit2.setDescription(unit.getDescription());
						unitsOb.set(index, unit2);
						UnitManager.update(unit2);

					}
				} else {
					unitsOb.remove(index);
					if (!isNew)
						UnitManager.delete(unit2.getId());
				}
			}
		});
		removeBtn.setOnAction(e -> {
			Unit unit = unitslist.getSelectionModel().getSelectedItem();
			unitsOb.remove(unit);
			UnitManager.delete(unit.getId());
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
}
