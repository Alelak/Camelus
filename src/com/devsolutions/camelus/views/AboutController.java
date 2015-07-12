package com.devsolutions.camelus.views;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import com.devsolutions.camelus.utils.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AboutController implements Initializable {
	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Hyperlink linkJavafx;
	@FXML
	private Hyperlink linkMySql;
	@FXML
	private Hyperlink linkSceneBuilder;
	@FXML
	private Hyperlink linkFontAwsome;
	@FXML
	private Hyperlink linkRobtoFont;
	@FXML
	private Hyperlink linkItextPFD;
	@FXML
	private Hyperlink linkMyBatis;
	@FXML
	private Hyperlink linkJson;
	@FXML
	private Hyperlink linkAlladin;
	@FXML
	private Hyperlink linkAmine;
	@FXML
	private Hyperlink linkBilel;
	@FXML
	private Hyperlink linkSaid;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXUtils.addDraggableNode(titleBar);
		linkJavafx.setOnMouseClicked(e -> runLink("http://www.oracle.com/technetwork/java/javafx/overview/index.html"));
		linkMySql.setOnMouseClicked(e -> runLink("https://www.mysql.fr/"));
		linkSceneBuilder.setOnMouseClicked(e -> runLink("http://www.oracle.com/technetwork/java/javafx/downloads/devpreview-1429449.html"));
		linkFontAwsome.setOnMouseClicked(e -> runLink("https://github.com/cathive/fonts-fontawesome"));
		linkRobtoFont.setOnMouseClicked(e -> runLink("https://www.google.com/fonts/specimen/Roboto"));
		linkItextPFD.setOnMouseClicked(e -> runLink("http://itextpdf.com/"));
		linkMyBatis.setOnMouseClicked(e -> runLink("https://mybatis.github.io/mybatis-3/"));
		linkJson.setOnMouseClicked(e -> runLink("http://json.org/"));
		linkAlladin.setOnMouseClicked(e -> runLink("https://www.linkedin.com/in/alladinelakhrass"));
		linkAmine.setOnMouseClicked(e -> runLink("https://www.linkedin.com/pub/massinissa-amine-aberbache/a7/14/b72"));
		linkBilel.setOnMouseClicked(e -> runLink("https://www.linkedin.com/pub/bilel-bouach/87/548/43"));
		linkSaid.setOnMouseClicked(e -> runLink("https://www.linkedin.com/pub/said-bejaoui/44/996/b40"));
	}

	@FXML
	private void CloseWindow() {
		Stage stage = (Stage) lblClose.getScene().getWindow();
		stage.close();
	}

	public void runLink(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e) {

			}
		}
	}

}
