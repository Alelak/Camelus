package com.devsolutions.camelus.application;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass()
					.getResource("../views/Login.fxml"));
			Scene scene = new Scene(root);
			Font.loadFont(getClass().getResource("../../../../fonts/Roboto-Medium.ttf").toExternalForm(), 10);
			scene.getStylesheets().add(
					getClass().getResource("../views/main.css")
							.toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
