package com.red_folder.phonegap.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHelper {

	public static void set(Context context, String key, String value) {
		SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        editor.commit(); // Very important
	}
	
	public static String get(Context context, String key) {
		SharedPreferences sharedPrefs = getSharedPreferences(context);  

		return sharedPrefs.getString(key, "");
	}
	
	private static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	private static SharedPreferences.Editor getEditor(Context context) {
		SharedPreferences sharedPrefs = getSharedPreferences(context);
		return sharedPrefs.edit();
	}

}
