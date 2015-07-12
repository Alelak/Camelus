package com.devsolutions.camelus;

import java.io.File;
import java.io.FileWriter;

import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.utils.DBConfig;
import com.devsolutions.camelus.utils.FXUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	public void init() throws Exception {
		super.init();
		File file1 = new File(System.getenv("APPDATA")
				+ "\\Camelus\\DBConfig.json");
		File file2 = new File(System.getenv("APPDATA")
				+ "\\Camelus\\AuditDBConfig.json");
		if (!file1.exists()) {
			DBConfig dbConfig = new DBConfig();
			dbConfig.setUsername("root");
			dbConfig.setPassword("abc123...");
			dbConfig.setIp("127.0.0.1");
			dbConfig.setSchema("camelus");
			dbConfig.setPort("3306");

			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson json = builder.create();

			file1.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(file1);
			writer.write(json.toJson(dbConfig));
			writer.close();
		}
		if (!file2.exists()) {
			DBConfig dbConfig = new DBConfig();
			dbConfig.setUsername("root");
			dbConfig.setPassword("abc123...");
			dbConfig.setIp("127.0.0.1");
			dbConfig.setSchema("camelus_audits");
			dbConfig.setPort("3306");

			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson json = builder.create();

			file2.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(file2);
			writer.write(json.toJson(dbConfig));
			writer.close();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		try {

			DBConnection.getSqlSessionFactory();
			Font.loadFont(
					Main.class.getResource("/fonts/fontawesome-webfont.ttf")
							.toExternalForm(), 14);

			FXMLLoader loader = new FXMLLoader(
					Main.class.getResource("views/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Font.loadFont(getClass().getResource("/fonts/Roboto-Medium.ttf")
					.toExternalForm(), 10);
			Font.loadFont(getClass().getResource("/fonts/Roboto-Bold.ttf")
					.toExternalForm(), 10);
			scene.getStylesheets().add(
					getClass().getResource("views/main.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setTitle("Camelus");
			primaryStage.getIcons().add(
					new Image(getClass().getResource("/images/logo.png")
							.toExternalForm()));
			primaryStage.show();
			FXUtils.setStage(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
