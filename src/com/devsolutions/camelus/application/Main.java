package com.devsolutions.camelus.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	private double initialX;
	private double initialY;

	@Override
	public void start(Stage primaryStage) {
		try {
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
					getClass().getResource(
							"../../../../fonts/Roboto-Bold.ttf")
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
			addDraggableNode(root);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void addDraggableNode(final Node node) {
		node.setOnMousePressed(e -> {
			if (e.getButton() != MouseButton.MIDDLE) {
				initialX = e.getSceneX();
				initialY = e.getSceneY();
			}
		});
		node.setOnMouseDragged(e -> {
			if (e.getButton() != MouseButton.MIDDLE) {
				node.getScene().getWindow().setX(e.getScreenX() - initialX);
				node.getScene().getWindow().setY(e.getScreenY() - initialY);
			}
		});
	}

}
