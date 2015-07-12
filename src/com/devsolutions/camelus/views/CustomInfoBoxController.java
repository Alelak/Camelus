package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.ResourceBundle;
import com.devsolutions.camelus.utils.FXUtils;
import com.devsolutions.camelus.utils.FontAwesomeIcon;
import com.devsolutions.camelus.utils.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomInfoBoxController implements Initializable {

	@FXML
	private Label lblClose;
	@FXML
	private GridPane toolBar;
	@FXML
	public Button btn;
	@FXML
	public Label msg;
	@FXML
	private Label titleBox;
	@FXML
	private FontAwesomeIconView iconBox;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(toolBar);
		btn.setOnAction(e -> {
			Stage stage = (Stage) btn.getScene().getWindow();
			stage.close();
		});
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}

	public void setMessage(String text) {
		msg.setText(text);
	}

	public void setBtnText(String text) {
		btn.setText(text);
	}

	
	public void setTitleBox(String text) {
		titleBox.setText(text);
	}
	
	public void setIconBox(FontAwesomeIcon icon, String color) {
		iconBox.setIcon(icon);
		iconBox.setStyle(color);
	}

}
