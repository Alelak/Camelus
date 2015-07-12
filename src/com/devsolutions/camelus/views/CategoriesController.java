package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.managers.CategoryManager;
import com.devsolutions.camelus.managers.ProductManager;
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
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class CategoriesController implements Initializable {
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
	@FXML
	private Label msgtxt;
	private boolean isNew;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
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
			msgtxt.setText("Saisissez une categorie puis entrez");
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
						if (!category.getDescription().isEmpty()) {
							if (!FXUtils.customcontains(categoriesOb,
									category.getDescription())) {
								if (isNew) {
									CategoryManager.add(category);

									categoriesOb.set(index, category);

								} else {

									category2.setDescription(category
											.getDescription());
									CategoryManager.update(category2);
									categoriesOb.set(index, category2);
								}
								msgtxt.setText("");
							} else {
								msgtxt.setText("Cette categorie existe deja!");
							}
						} else {

							if (!isNew) {
								if (ProductManager.getByCategory(
										category.getId()).isEmpty()) {
									categoriesOb.remove(index);
									CategoryManager.delete(category2.getId());
									msgtxt.setText("");
								} else {
									msgtxt.setText("Cette categorie est lié avec un produit!");
								}
							}
						}
					}
				});
		removeBtn.setOnAction(e -> {
			Category category = categorielist.getSelectionModel()
					.getSelectedItem();
			if (ProductManager.getByCategory(category.getId()).isEmpty()) {

				categoriesOb.remove(category);
				CategoryManager.delete(category.getId());
				msgtxt.setText("");
			} else {
				msgtxt.setText("Cette categorie est lié avec un produit!");
			}
		});
		categoriesOb = FXCollections.observableArrayList(CategoryManager
				.getAll());
		if (!categoriesOb.isEmpty()) {
			categoriesOb.remove(0);
		}
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
}
