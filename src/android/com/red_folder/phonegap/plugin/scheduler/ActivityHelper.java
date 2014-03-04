package com.red_folder.phonegap.plugin.scheduler;

import com.red_folder.phonegap.plugin.scheduler.models.Alarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class ActivityHelper {

	public static final String WAKESCREEN = "WAKESCREEN";
	
	public static void onCreate(Activity activity) {
		Bundle extras = activity.getIntent().getExtras();
		
		if (extras != null) {
			if (extras.containsKey(WAKESCREEN))
				if (extras.getBoolean(WAKESCREEN, false))
					activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		}
		
	}

	public static Bundle createExtras(Alarm alarm) {
		Bundle bundle = new Bundle();
		
		if (alarm.getWakeScreen())
			bundle.putBoolean(WAKESCREEN, true);
		
		return bundle;
	}
}
