package com.red_folder.phonegap.plugin.scheduler;

import com.red_folder.phonegap.helpers.AppHelper;
import com.red_folder.phonegap.plugin.scheduler.dal.SchedulerDAL;
import com.red_folder.phonegap.plugin.scheduler.models.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

	private final static String TAG = AlarmBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Starting onReceive of broadcast");

		Bundle extras = intent.getExtras();

		if (extras == null || !extras.containsKey("id")) {
			Log.d(TAG, "Extras is null or doesn't inlcude an ID, thus ignoring broadcast");
		} else {
			SchedulerDAL dal = new SchedulerDAL(context);

			int id = extras.getInt("id");
			Alarm alarm = dal.get(id);
			

			if (alarm == null) {
				Log.d(TAG, "No alarm found for given ID, thus ignoring broadcast");
			} else {
				
				dal.delete(alarm);
				dal.save();
				
				PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
				//Acquire the lock
				wl.acquire();

				Log.d(TAG, "Asked to Start app - " + alarm.getClassName());

				if (alarm.getClassName() != null) {
					//commented out to ensure it's brought to foreground even if active in background
					// note: it then requires in Android-manifest: android:launchMode="singleTask" or android:launchMode="singleIstance"
					//if (AppHelper.isServiceRunning(context, alarm.getClassName())) {
					//	Log.d(TAG, alarm.getClassName() + " already running");
					//} else {
						Log.d(TAG, alarm.getClassName() + " starting");
						AppHelper.startActivity(context, alarm.getClassName());
					//}
				}
				
				//Release the lock
				wl.release();
			}
			
			dal = null;

		}

	}
}
