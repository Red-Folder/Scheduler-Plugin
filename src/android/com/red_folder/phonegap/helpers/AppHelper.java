package com.red_folder.phonegap.helpers;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppHelper {

	private static final String TAG = AppHelper.class.getSimpleName();

	/*
	 ************************************************************************************************
	 * Helper function 
	 ************************************************************************************************
	 */

	public static boolean isServiceRunning(Context context, String className) { 
		boolean isServiceFound = false;
		
		try {
			ActivityManager activityManager = (ActivityManager)context.getSystemService (Context.ACTIVITY_SERVICE); 
			List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE); 
		    for (int i = 0; i < services.size(); i++) { 
		        if (services.get(i).topActivity.getClassName().equalsIgnoreCase(className)) {
		            isServiceFound = true;
		        }
		    }
		} catch (Exception ex) {
		} 

	    return isServiceFound; 
	 }
	
	public static boolean startActivity(Context context, String className) {
		boolean result = false;
		
		Log.d(TAG, "Attempting to start " + className);
		try {
			Intent intent = new Intent();
			intent.setClassName(context, className);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			
			Log.d(TAG, "Activity started");
		} catch (Exception ex) {
			Log.d(TAG, "Activity failed to start - " + ex.getMessage());
		}
		
		return result;
	}

}
