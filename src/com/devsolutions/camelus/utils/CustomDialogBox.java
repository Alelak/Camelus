package com.devsolutions.camelus.utils;

import java.io.IOException;

import com.devsolutions.camelus.controllers.CustomDialogBoxController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomDialogBox {
	private Parent root;
	private Scene scene;
	public Button positiveButton;
	public Button negativeButton;
	public Stage stage;

	public CustomDialogBox(Stage mainStage, String message,
			String positiveButtonText, String negativeButtonText)
			throws IOException {
		this.stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../views/CustomDialogBox.fxml"));
		root = loader.load();
		CustomDialogBoxController dialogBoxController = loader
				.<CustomDialogBoxController> getController();
		positiveButton = dialogBoxController.positiveButton;
		negativeButton = dialogBoxController.negativeButton;
		dialogBoxController.setMessage(message);
		dialogBoxController.setPositiveButtonText(positiveButtonText);
		dialogBoxController.setNegativeButtonText(negativeButtonText);
		negativeButton.setOnAction(e -> stage.close());
		scene = new Scene(root);
		scene.getStylesheets().add(
				getClass().getResource("../views/CustomDialogBox.css")
						.toExternalForm());
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(mainStage);
		stage.show();
		FXUtils.centerStage(mainStage, stage, 18);
	}
}