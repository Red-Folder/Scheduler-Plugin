package com.red_folder.phonegap.plugin.scheduler;

import java.util.Iterator;

import org.apache.cordova.CordovaActivity;
import org.json.JSONException;
import org.json.JSONObject;

import com.red_folder.phonegap.helpers.Log;
import com.red_folder.phonegap.plugin.scheduler.models.Alarm;
import com.red_folder.phonegap.plugin.scheduler.models.ShowOption;

import android.os.Bundle;
import android.view.WindowManager;

public class ActivityHelper {

	private static final String TAG = ActivityHelper.class.getSimpleName();
	
	public static final String SHOW_OPTION = "SHOWOPTION";
	
	public static void onCreate(CordovaActivity activity) {
		Bundle extras = activity.getIntent().getExtras();
		
		if (extras != null) {
			if (extras.containsKey(SHOW_OPTION)) {
				ShowOption option = ShowOption.parse(extras.getString(SHOW_OPTION));
				
				if (option == ShowOption.WakeScreen)
					activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				
				if (option == ShowOption.ShowOverLockScreen)
					activity.getWindow().addFlags(	WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
													WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
				            						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
				            						WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			}
				
		}
	}
	
	
	public static Bundle createExtras(Alarm alarm) {
		Bundle bundle = new Bundle();

		bundle.putString(SHOW_OPTION, alarm.getShowOption().toString());
	
		JSONObject extras = alarm.getExtras();
		if (extras != null) {
			Iterator<?> keys = extras.keys();
			
			while (keys.hasNext()) {
				String key = (String)keys.next();
				try {
					Object value = extras.get(key);
					if (value instanceof String)
						bundle.putString(key, (String)value);
					if (value instanceof Integer)
						bundle.putInt(key, (Integer)value);
					if (value instanceof Boolean)
						bundle.putBoolean(key, (Boolean)value);
					
				} catch (JSONException ex) {
					Log.d(TAG, "Error retriving value for key (" + key + ")", ex);
				}
			}
		}
	

		return bundle;
	}
	
}
