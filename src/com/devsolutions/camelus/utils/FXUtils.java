package com.devsolutions.camelus.utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;

public class FXUtils {
	private static double initialX;
	private static double initialY;

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
}
