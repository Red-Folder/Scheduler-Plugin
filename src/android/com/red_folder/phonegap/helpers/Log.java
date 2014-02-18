package com.red_folder.phonegap.helpers;

public class Log {

	public static void d(String tag, String message) {
		android.util.Log.d(tag, message);
	}

	public static void d(String tag, String message, Exception ex) {
		d(tag, message);
		
		if (ex != null) {
			d(tag, ex.toString());
			
			if (ex.getMessage() != null)
				d(tag, ex.getMessage());
			
			if (ex.getStackTrace() != null) {
				for (int i =0; i < ex.getStackTrace().length; i++) {
					d(tag, ex.getStackTrace()[i].toString());
				}
			}
		}
	}

}
