package com.red_folder.phonegap.plugin.scheduler;

import com.red_folder.phonegap.plugin.scheduler.models.Alarm;
import com.red_folder.phonegap.plugin.scheduler.models.ShowOption;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class ActivityHelper {

	public static final String SHOW_OPTION = "SHOWOPTION";
	
	public static void onCreate(Activity activity) {
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
		
		return bundle;
	}
}
