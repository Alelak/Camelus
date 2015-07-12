package com.devsolutions.camelus.views;

import java.io.IOException;

import com.devsolutions.camelus.utils.FXUtils;

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
	public Button btn;
	public Stage stage;

	public CustomInfoBox(Stage mainStage, String message, String btnText,
			String color) throws IOException {
		this.stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"CustomInfoBox.fxml"));
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
				getClass().getResource("CustomDialogBox.css").toExternalForm());
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(mainStage);
		stage.show();
		FXUtils.centerStage(mainStage, stage, 18);
	}

	public CustomInfoBox(Stage stage, String message, String btnText)
			throws IOException {
		this(stage, message, btnText, "#000000");
	}

}