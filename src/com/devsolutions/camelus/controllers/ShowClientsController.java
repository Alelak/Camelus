package com.devsolutions.camelus.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.entities.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ShowClientsController implements Initializable {
	@FXML GridPane motherGrid;
	@FXML GridPane content;
	
	
	@FXML RowConstraints rowOne;
	@FXML VBox gridRowOne;
	@FXML Label message1;
	
	@FXML RowConstraints rowTwo;
	@FXML GridPane gridRowTwo;
	@FXML TextField textFieldSearch;
	@FXML Button btnSearch;
	@FXML Button btnRefresh;
	@FXML TableView<Client> tableView;
	
	@FXML Button btnAdd;
	@FXML Button btnUpdate;
	@FXML Button btnDelete;
	@FXML Button btnConsult;
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		content.getChildren().remove(gridRowTwo);
		rowOne.setPercentHeight(100);
		rowTwo.setPercentHeight(0);
		btnUpdate.setDisable(true);
		btnDelete.setDisable(true);
		btnConsult.setDisable(true);
		btnAdd.setOnAction(e-> showAddClientWindow());
		//btnUpdate.setOnAction(e-> );
	}
	
	private void showTableView(){
		content.getChildren().remove(gridRowOne);
		content.getChildren().add(gridRowTwo);
		rowOne.setPercentHeight(0);
		rowTwo.setPercentHeight(100);
		btnUpdate.setDisable(false);
		btnDelete.setDisable(false);
		btnConsult.setDisable(false);
	}
	

	private void showAddClientWindow(){
		Stage stage = new Stage();
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../views/AddClient.fxml"));
		try {
			 root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(motherGrid.getScene().getWindow());
		stage.show();
	}

	
}
