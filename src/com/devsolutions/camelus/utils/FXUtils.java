package com.devsolutions.camelus.utils;

import java.io.IOException;

import com.devsolutions.camelus.views.CustomInfoBox;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class FXUtils {

	private static Stage stage;
	private static double initialX;
	private static double initialY;
	public static final String HAS_ERROR = "-fx-border-color: red;-fx-border-width: 2; -fx-focus-color: transparent;";
	public static final String HAS_SUCCESS = "-fx-border-color: green;-fx-border-width: 2; -fx-focus-color: transparent;";

	public static <T> boolean customcontains(ObservableList<T> observableList,
			String string) {
		boolean found = false;
		for (Object object : observableList) {
			if (object.toString().equals(string))
				found = true;
		}

		return found;
	}

	public static void addDraggableNode(final Node node) {
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

	public static void centerStage(Stage parentStage, Stage childStage, int y) {
		childStage.setX(parentStage.getX() + parentStage.getWidth() / 2
				- childStage.getWidth() / 2);
		childStage
				.setY((parentStage.getY() + parentStage.getHeight() / 2 - childStage
						.getHeight() / 2) + y);
	}

	public static void openDBErrorDialog() {
		try {
			new CustomInfoBox(stage,
					"Echec de connexion a la base de donnee \n"
							+ "Impossible de ouvrir la fenetre demande", "Ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		FXUtils.stage = stage;
	}

}
