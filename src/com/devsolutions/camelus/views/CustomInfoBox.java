package com.devsolutions.camelus.views;

import java.io.IOException;
import com.devsolutions.camelus.utils.BoxType;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.FontAwesomeIcon;
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
	private String titleBox;
	private FontAwesomeIcon iconBox;
	private String iconBoxColor;
	public CustomInfoBox(Stage mainStage,BoxType boxType, String message, String btnText) throws IOException {	
		switch (boxType) {
		case ERROR:
			this.titleBox = "Erreur";
			this.iconBox = FontAwesomeIcon.ICON_REMOVE_CIRCLE;
			this.iconBoxColor = "-fx-text-fill: #b72f2f";
			break;
		case WARNING:
			this.titleBox = "Attention";
			this.iconBox = FontAwesomeIcon.ICON_EXCLAMATION_SIGN;
			this.iconBoxColor = "-fx-text-fill: #e56110";
			break;
		case INFORMATION:
			this.titleBox = "Information";
			this.iconBox = FontAwesomeIcon.ICON_INFO_SIGN;
			this.iconBoxColor = "-fx-text-fill: #3b76ba";
			break;
		case QUESTION:
			this.titleBox = "Question";
			this.iconBox = FontAwesomeIcon.ICON_QUESTION_SIGN;
			this.iconBoxColor = "-fx-text-fill: #a2b292"; 
			break;
		case SUCCESS:
			this.titleBox = "Succès";
			this.iconBox = FontAwesomeIcon.ICON_CHECK_SIGN;
			this.iconBoxColor = "-fx-text-fill: #398439";
			break;
		}		
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
		infoBoxController.setTitleBox(titleBox);
		infoBoxController.setIconBox(iconBox,iconBoxColor);
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

}