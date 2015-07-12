package com.devsolutions.camelus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String monthName(int month) {
		String monthName = "";

		switch (month) {
		case 1:
			monthName = "Janvier";
			break;
		case 2:
			monthName = "Fevrier";
			break;
		case 3:
			monthName = "Mars";
			break;
		case 4:
			monthName = "Avril";
			break;
		case 5:
			monthName = "Mai";
			break;
		case 6:
			monthName = "Juin";
			break;
		case 7:
			monthName = "Juillet";
			break;
		case 8:
			monthName = "Aout";
			break;
		case 9:
			monthName = "Septembre";
			break;
		case 10:
			monthName = "Octobre";
			break;
		case 11:
			monthName = "Novembre";
			break;
		case 12:
			monthName = "Decembre";
			break;
		}

		return monthName;
	}

	public static String formatDate(Date date) {
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}

	public static boolean validPhoneNumber(String phoneNo) {
		String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNo);
		return matcher.matches();
	}

	public static boolean validEmail(String email) {
		Pattern ptr = Pattern
				.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$");
		return (ptr.matcher(email).matches()) ? true : false;
	}
}
