package com.jlaq.keepscreenon;

public class Util {

	/**
	 * Displays the timeout in a more human-friendly format
	 */
	public static String getProperTime(int timeout) {
		if (timeout == Integer.MAX_VALUE)
			return "Never";

		long seconds = timeout / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		String timeoutString = " ";
		if (days != 0) timeoutString += days + " days ";
		if (hours % 24 != 0) timeoutString += hours % 24 + " hrs ";
		if (minutes % 60 != 0) timeoutString += minutes % 60 + " min ";
		if (seconds % 60 != 0) timeoutString += seconds % 60 + " s ";

		return timeoutString;
	}
}
