package com.jlaq.keepscreenon;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPrefsManager {
	// Shared preferences variables
	private SharedPreferences sharedPreferences;

	// Shared preferences keys
	private final String SHAREDPREFS_TIMEOUT = "saved_timeout";

	/**
	 * Constructor
	 *
	 * @param context context
	 */
	public MySharedPrefsManager(Context context) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Get saved timeout
	 *
	 * @return int saved timeout
	 */
	public int getTimeout() {
		return sharedPreferences.getInt(SHAREDPREFS_TIMEOUT, -1);
	}

	/**
	 * Save timeout
	 *
	 * @param timeout int timeout to save
	 */
	public void saveTimeout(int timeout) {
		sharedPreferences.edit().putInt(SHAREDPREFS_TIMEOUT, timeout).apply();
	}

}

