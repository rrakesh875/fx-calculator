package com.fx.calculator.utility;

/**
 * Utility class
 * @author Ravi Rakesh
 *
 */
public class FXUtility {
	
	private static StringBuilder stringBuilder = new StringBuilder();
	
	public static boolean isInValidEntry(String choice) {
		return (choice==null) || (choice.isEmpty()) || ("null".equalsIgnoreCase(choice));
	}

	public static StringBuilder getStringBuilder() {
		return stringBuilder;
	}

}
