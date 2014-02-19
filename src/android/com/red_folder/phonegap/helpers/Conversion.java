package com.red_folder.phonegap.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Conversion {
	
	private static final String TAG = Conversion.class.getSimpleName();
	
	private final static String JAVASCRIPT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public static Date convertJavaScriptDateToDate(String javascriptDate) {
		DateFormat format = new SimpleDateFormat(JAVASCRIPT_DATE_FORMAT);
		Date result = null;
		
		try { result = format.parse(javascriptDate); } catch (ParseException ex) { Log.d(TAG, "Unable to convert provided JavaScript string to Java Date", ex); }
		
		return result;		
	}
	
	public static String convertDateToJavaScriptDate(Date javaDate) {
		DateFormat format = new SimpleDateFormat(JAVASCRIPT_DATE_FORMAT);
		String result = null;
		
		result = format.format(javaDate);
		
		return result;
	}

}
