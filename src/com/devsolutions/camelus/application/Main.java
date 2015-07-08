package com.devsolutions.camelus.application;

import com.devsolutions.camelus.services.DBConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			DBConnection.getSqlSessionFactory();
			Font.loadFont(
					Main.class.getResource(
							"../../../../fonts/fontawesome-webfont.ttf")
							.toExternalForm(), 14);
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"../views/login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Font.loadFont(
					getClass().getResource(
							"../../../../fonts/Roboto-Medium.ttf")
							.toExternalForm(), 10);
			Font.loadFont(
					getClass().getResource("../../../../fonts/Roboto-Bold.ttf")
							.toExternalForm(), 10);
			scene.getStylesheets().add(
					getClass().getResource("../views/main.css")
							.toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setTitle("Camelus");
			primaryStage.getIcons().add(
					new Image(getClass().getResource(
							"../../../../images/logo.png").toExternalForm()));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
