package com.devsolutions.camelus.utils;


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
	
	public static String formateDate(int day, int month, int year) {
		String formatedDate  = "";
		String formatedDay   = "";
		String formatedMonth = "";
		
		if(day<10)
			formatedDay = "0" + day;
		else
			formatedDay = "" + day;
		
		if(month<10)
			formatedMonth = "0" + month;
		else
			formatedMonth = "" + month;
				
		formatedDate = formatedDay + "-" + formatedMonth + "-" + year;
		
		return formatedDate;
	}
	
	public static String monthName(int month) {
		String monthName  = "";
		
		switch(month)
		{
			case 1:
				monthName = "Janvier";
				break;
			case 2:
				monthName = "Février";
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
				monthName = "Décembre";
				break;
		}
		
		return monthName;
	}
}
