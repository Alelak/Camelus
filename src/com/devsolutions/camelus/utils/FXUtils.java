package com.devsolutions.camelus.utils;

import javafx.collections.ObservableList;

public class FXUtils {
	public static <T> boolean customcontains(ObservableList<T> observableList,
			String string) {
		boolean found = false;
		for (Object object : observableList) {
			if (object.toString().equals(string))
				found = true;
		}

		return found;
	}
}
