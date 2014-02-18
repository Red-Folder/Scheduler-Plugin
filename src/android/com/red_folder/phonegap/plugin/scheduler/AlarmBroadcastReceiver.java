package com.red_folder.phonegap.plugin.scheduler;

import com.red_folder.phonegap.helpers.AppHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

	private final static String TAG = AlarmBroadcastReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		//Acquire the lock
		wl.acquire();

		String appClassName = "com.example.scheduler.Scheduler";
		
		Log.d(TAG, "Asked to Start app - " + appClassName);
		
		if (appClassName != null) {
			if (AppHelper.isServiceRunning(context, appClassName)) {
				Log.d(TAG, appClassName + " already running");
			} else {
				Log.d(TAG, appClassName + " starting");
				AppHelper.startActivity(context, appClassName);
			}
		}
		
        //Release the lock
        wl.release();
	}

}
