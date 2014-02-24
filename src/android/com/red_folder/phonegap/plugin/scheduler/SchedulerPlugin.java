package com.red_folder.phonegap.plugin.scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import com.red_folder.phonegap.helpers.Log;
import com.red_folder.phonegap.plugin.scheduler.dal.SchedulerDAL;
import com.red_folder.phonegap.plugin.scheduler.models.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SchedulerPlugin  extends CordovaPlugin {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private static final String TAG = SchedulerPlugin.class.getSimpleName();

	/*
	 ************************************************************************************************
	 * Keys 
	 ************************************************************************************************
	 */
	public static final String ACTION_ADD_ALARM = "addAlarm";
	public static final String ACTION_CANCEL_ALARM = "cancelAlarm";

	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Overriden Methods 
	 ************************************************************************************************
	 */
	@Override
	public boolean execute(final String action, final JSONArray data, final CallbackContext callback) {
		boolean result = false;

		Log.d(TAG, "start of Execute");
		
		try {

			Log.d(TAG, "Action: " + action);

			if (ACTION_ADD_ALARM.equals(action) ||
				ACTION_CANCEL_ALARM.equals(action)) { 
			
				Log.d(TAG, "Setting up the runnable thread");
				
				final Context context = this.cordova.getActivity();
				cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						PluginResult pluginResult = null;
						if (ACTION_ADD_ALARM.equals(action)) {
							try {
								if (data.length() == 2) {
									Log.d(TAG, "Received, what = " + data.getString(0) + ", when = " + data.getString(1));

									String what = data.getString(0);
									String whenTxt = data.getString(1);

									Log.d(TAG, "Attempting to parse");
									//DateFormat m_ISO8601Local = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ssZ");
									//Date when = m_ISO8601Local.parse(whenTxt);
									DateFormat m_ISO8601Compliant = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
									Date when = m_ISO8601Compliant.parse(whenTxt);

									Log.d(TAG, "Run set alarm");
									pluginResult = addAlarm(context, what, when);
								} else {
									pluginResult = new PluginResult(Status.ERROR, "Expected 2 paramaters (what & when), only received " + data.length());
								}
								
							} catch (JSONException ex) {
								Log.d(TAG, "Error occurred when trying to get date", ex);
							} catch (ParseException ex) {
								Log.d(TAG, "Error occurred when trying to get date", ex);
							}
							//Date when = new Date(data.getString(0));
							//pluginResult = setAlarm(context, when);
						}
					
						if (ACTION_CANCEL_ALARM.equals(action))
							pluginResult = cancelAlarm(context);

						if (pluginResult == null) {
							Log.d(TAG, "No pluginResult generated, assume unknown action");
							pluginResult = new PluginResult(Status.INVALID_ACTION);
						} else {
							Log.d(TAG, "pluginResult = " +  pluginResult.toString());
							Log.d(TAG, "pluginResult.getMessage() = " +  pluginResult.getMessage());
						}

						callback.sendPluginResult(pluginResult);
					}
				});

				result = true;
			} else {
				result = false;
			}
			
		} catch (Exception ex) {
			Log.d(TAG, "Exception - " + ex.getMessage());
		}

		Log.d(TAG, "end of Execute");

		return result;
	}
	
	private PluginResult addAlarm(Context context, String what, Date when){
		PluginResult result = null;

		// Create an alarm record
		Alarm alarm = new Alarm();
		alarm.setClassName(what);
		alarm.setWhen(when);

		// Now save the alarm
		SchedulerDAL dal = new SchedulerDAL(context);
		int id = dal.add(alarm);
		if (id > 0) {
			dal.save();
		
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
			Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
			intent.putExtra("id", alarm.getId());
		
			PendingIntent pi = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
			
			am.set(AlarmManager.RTC_WAKEUP, alarm.getWhen().getTime(), pi);
		
			result = new PluginResult(Status.OK);
		} else {
			result = new PluginResult(Status.ERROR, "Unable to save Alarm details");
		}
		
		return result;
    }
	
	private PluginResult cancelAlarm(Context context)
    {
		PluginResult result = null;

		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);

		result = new PluginResult(Status.OK);
		return result;
    }


}
