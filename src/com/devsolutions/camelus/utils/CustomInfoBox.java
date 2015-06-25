package com.devsolutions.camelus.utils;

import java.io.IOException;

import com.devsolutions.camelus.controllers.CustomInfoBoxController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomInfoBox {
	private Parent root;
	private Scene scene;
	private Stage MainStage;
	public Button btn;

	public CustomInfoBox(Stage stage, String message, String btnText,
			String color) throws IOException {
		MainStage = stage;
		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../views/CustomInfoBox.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		CustomInfoBoxController infoBoxController = loader
				.<CustomInfoBoxController> getController();
		btn = infoBoxController.btn;

		infoBoxController.setMessage(message);
		infoBoxController.setBtnText(btnText);
		infoBoxController.setTextColor(color);

		scene = new Scene(root);
		scene.getStylesheets().add(
				getClass().getResource("../views/CustomDialogBox.css")
						.toExternalForm());
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(MainStage);
		stage.show();
	}

	public CustomInfoBox(Stage stage, String message, String btnText) throws IOException {
		this(stage, message, btnText, "#000000");
	}
}