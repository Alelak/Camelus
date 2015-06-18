package com.devsolutions.camelus.application;

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
	private Stage MainStage;
	public Button positiveButton;
	public Button negativeButton;

	public CustomDialogBox(Stage stage, String message,
			String positiveButtonText, String negativeButtonText)
			throws IOException {
		MainStage = stage;
		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../views/CustomDialogBox.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		CustomDialogBoxController dialogBoxController = loader
				.<CustomDialogBoxController> getController();
		positiveButton = dialogBoxController.positiveButton;
		negativeButton = dialogBoxController.negativeButton;
		dialogBoxController.setMessage(message);
		dialogBoxController.setPositiveButtonText(positiveButtonText);
		dialogBoxController.setNegativeButtonText(negativeButtonText);
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

}