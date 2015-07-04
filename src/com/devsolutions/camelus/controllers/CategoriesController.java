package com.devsolutions.camelus.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.managers.CategoryManager;

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
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class CategoriesController implements Initializable {
	private double initialX;
	private double initialY;
	@FXML
	private GridPane titleBar;
	@FXML
	private ListView<Category> categorielist;
	@FXML
	private ObservableList<Category> categoriesOb;
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
		categorielist.setPlaceholder(new Label("Pas de Categories"));
		lblClose.setOnMouseClicked(e -> {

			((Stage) lblClose.getScene().getWindow()).close();

		});
		categorielist.getSelectionModel()
				.setSelectionMode(SelectionMode.SINGLE);
		final Timeline animation = new Timeline(new KeyFrame(
				Duration.seconds(.1), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						categorielist.edit(categorielist.getSelectionModel()
								.getSelectedIndex());
					}
				}));
		animation.setCycleCount(1);

		addBtn.setOnAction(e -> {
			categorielist.getItems().add(new Category(""));
			categorielist.getSelectionModel().selectLast();
			animation.play();
		});
		categorielist.setCellFactory(TextFieldListCell
				.forListView(new StringConverter<Category>() {

					@Override
					public String toString(Category object) {

						return object.toString();
					}

					@Override
					public Category fromString(String string) {

						return new Category(string);
					}

				}));
		categorielist
				.setOnEditStart(new EventHandler<ListView.EditEvent<Category>>() {

					@Override
					public void handle(EditEvent<Category> event) {
						if (categoriesOb.get(event.getIndex()).getId() == 0) {
							isNew = true;
						} else {
							isNew = false;
						}

					}
				});
		categorielist
				.setOnEditCommit(new EventHandler<ListView.EditEvent<Category>>() {

					@Override
					public void handle(EditEvent<Category> event) {
						Category category = event.getNewValue();
						int index = event.getIndex();
						Category category2 = categoriesOb.get(index);
						if (!event.getNewValue().getDescription().isEmpty()) {
							if (isNew) {
								CategoryManager.add(category);

								categoriesOb.set(index, category);
							} else {

								category2.setDescription(category
										.getDescription());
								CategoryManager.update(category2);
								categoriesOb.set(index, category2);
							}
						} else {
							categoriesOb.remove(index);
							if (!isNew)
								CategoryManager.delete(category2.getId());
						}
					}
				});
		removeBtn.setOnAction(e -> {
			Category category = categorielist.getSelectionModel()
					.getSelectedItem();
			categoriesOb.remove(category);
			CategoryManager.delete(category.getId());
		});
		categoriesOb = FXCollections.observableArrayList(CategoryManager
				.getAll());
		categoriesOb.remove(0);
		categorielist.setItems(categoriesOb);
		categorielist.setEditable(true);
		categorielist.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Category>() {

					@Override
					public void changed(
							ObservableValue<? extends Category> observable,
							Category oldValue, Category newValue) {
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
