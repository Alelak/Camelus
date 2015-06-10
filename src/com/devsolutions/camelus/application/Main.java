package com.devsolutions.camelus.application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//salut a tous 
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("../views/Main.fxml"));
			Scene scene = new Scene(root,400,400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//bonjour c siaka
	public static void main(String[] args) {
		launch(args);
		// Modified by Massi
	}
}
