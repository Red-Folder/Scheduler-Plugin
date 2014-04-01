package com.red_folder.phonegap.plugin.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.red_folder.phonegap.helpers.Log;
import com.red_folder.phonegap.plugin.scheduler.dal.SchedulerDAL;
import com.red_folder.phonegap.plugin.scheduler.models.Alarm;
import com.red_folder.phonegap.plugin.scheduler.models.ShowOption;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
	public static final String ACTION_GET_ALARM = "getAlarm";
	public static final String ACTION_GET_ALARMS = "getAlarms";
	public static final String ACTION_UPDATE_ALARM = "updateAlarm";
	public static final String ACTION_CANCEL_ALARM = "cancelAlarm";
	public static final String ACTION_GET_DEFAULT_CLASS = "getDefaultClass";
	public static final String ACTION_GET_ACTIVITY_EXTRAS = "getActivityExtras";

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
				ACTION_GET_ALARM.equals(action) ||
				ACTION_GET_ALARMS.equals(action) ||
				ACTION_CANCEL_ALARM.equals(action) ||
				ACTION_UPDATE_ALARM.equals(action) ||
				ACTION_GET_DEFAULT_CLASS.equals(action) ||
				ACTION_GET_ACTIVITY_EXTRAS.equals(action)) { 
			
				Log.d(TAG, "Setting up the runnable thread");
				
				final Context context = this.cordova.getActivity();
				cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						PluginResult pluginResult = null;
						
						if (ACTION_ADD_ALARM.equals(action)) {
							try {
								if (data.length() == 4) {
									Log.d(TAG, "Received, what = " + data.getString(0) + ", " + 
								                         "when = " + data.getString(1) + ", " +
					                                     "showOption = " + data.getString(2) + ", " +
								                         "extras = " + data.getJSONObject(3).toString());

									String what = data.getString(0);
									String whenTxt = data.getString(1);
									ShowOption showOption = ShowOption.parse(data.getString(2));
									JSONObject extras = data.getJSONObject(3);

									Log.d(TAG, "Attempting to parse");
									DateFormat m_ISO8601Compliant = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
									Date when = m_ISO8601Compliant.parse(whenTxt);

									Log.d(TAG, "Run set alarm");
									pluginResult = addAlarm(context, what, when, showOption, extras);
								} else {
									pluginResult = new PluginResult(Status.ERROR, "Expected 4 paramaters (what, when, showOption, extras), only received " + data.length());
								}
								
							} catch (JSONException ex) {
								Log.d(TAG, "Error occurred when trying to get date", ex);
							} catch (ParseException ex) {
								Log.d(TAG, "Error occurred when trying to get date", ex);
							}
						}
					
						if (ACTION_GET_ALARM.equals(action))
							try {
								pluginResult = getAlarm(context, data.getInt(0));
							} catch (JSONException ex) {
								Log.d(TAG, "Error occurred when trying to get ID", ex);
							}

						if (ACTION_GET_ALARMS.equals(action))
							pluginResult = getAlarms(context);
						
						if (ACTION_CANCEL_ALARM.equals(action))
							try {
								pluginResult = cancelAlarm(context, data.getInt(0));
							} catch (JSONException ex) {
								Log.d(TAG, "Error occurred when trying to get ID", ex);
							}

						if (ACTION_UPDATE_ALARM.equals(action)) {
							try {
								if (data.length() == 5) {
									Log.d(TAG, "Received id = " + data.getInt(0) + ", " +
											            "what = " + data.getString(1) + ", " + 
					                                    "when = " + data.getString(2) + "," +
					                                    "showOption = " + data.getString(3) + ", " +
										                "extras = " + data.getJSONObject(4).toString());

									int id = data.getInt(0);
									String what = data.getString(1);
									String whenTxt = data.getString(2);
									ShowOption showOption = ShowOption.parse(data.getString(3));
									JSONObject extras = data.getJSONObject(4);

									Log.d(TAG, "Attempting to parse");
									DateFormat m_ISO8601Compliant = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
									Date when = m_ISO8601Compliant.parse(whenTxt);

									Log.d(TAG, "Run update alarm");
									pluginResult = updateAlarm(context, id, what, when, showOption, extras);
								} else {
									pluginResult = new PluginResult(Status.ERROR, "Expected 5 paramaters (id, what, when, showOption, extras), only received " + data.length());
								}
								
							} catch (JSONException ex) {
								Log.d(TAG, "Error occurred when trying to get date", ex);
							} catch (ParseException ex) {
								Log.d(TAG, "Error occurred when trying to get date", ex);
							}
						}

						if (ACTION_GET_DEFAULT_CLASS.equals(action))
							pluginResult = getDefaultClass(context);
						
						if (ACTION_GET_ACTIVITY_EXTRAS.equals(action))
							pluginResult = getActivityExtras(context);
						
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
	
	private PluginResult addAlarm(Context context, String what, Date when, ShowOption showOption, JSONObject extras){
		PluginResult result = null;

		// Create an alarm record
		Alarm alarm = new Alarm();
		alarm.setClassName(what);
		alarm.setWhen(when);
		alarm.setShowOption(showOption);
		alarm.setExtras(extras);

		// Now save the alarm
		SchedulerDAL dal = new SchedulerDAL(context);
		int id = dal.add(alarm);
		if (id > 0) {
			dal.save();
		
			addToAlarmManager(context, alarm);
		
			result = new PluginResult(Status.OK, dal.getAsJSON(id));
		} else {
			result = new PluginResult(Status.ERROR, "Unable to save Alarm details");
		}
		
		return result;
    }
	
	private PluginResult getAlarm(Context context, int id){
		SchedulerDAL dal = new SchedulerDAL(context);
		PluginResult result = new PluginResult(Status.OK, dal.getAsJSON(id));
		return result;
    }
	
	private PluginResult getAlarms(Context context){
		SchedulerDAL dal = new SchedulerDAL(context);
		PluginResult result = new PluginResult(Status.OK, dal.getAsJSON());
		return result;
    }
	
	private PluginResult cancelAlarm(Context context, int id)
    {
		PluginResult result = null;

		Alarm alarm = new Alarm();
		alarm.setId(id);
		
		SchedulerDAL dal = new SchedulerDAL(context);
        dal.delete(alarm);
        dal.save();
        dal = null;
        
		cancelFromAlarmManager(context, alarm);

		result = new PluginResult(Status.OK);
		return result;
    }

	private PluginResult updateAlarm(Context context, int id, String what, Date when, ShowOption showOption, JSONObject extras)
    {
		PluginResult result = null;

		Alarm alarm = new Alarm();
		alarm.setId(id);
		alarm.setClassName(what);
		alarm.setWhen(when);
		alarm.setShowOption(showOption);
		alarm.setExtras(extras);

		SchedulerDAL dal = new SchedulerDAL(context);
		dal.update(alarm);
		dal.save();
		
		cancelFromAlarmManager(context, alarm);
		addToAlarmManager(context, alarm);

		result = new PluginResult(Status.OK, dal.getAsJSON(id));

		return result;
    }

	private PluginResult getDefaultClass(Context context) {
		PluginResult result = null;
		
		result = new PluginResult(Status.OK, context.getClass().getName());
		
		return result;
	}

	private PluginResult getActivityExtras(Context context) {
		PluginResult result = null;
		JSONObject data = new JSONObject();
		
		try {
			CordovaActivity activity = (CordovaActivity)context;
			Bundle extras = activity.getIntent().getExtras();
			
			if (extras != null) {
				Set<String> keys = extras.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object value = extras.get(key);
					data.put(key, value);
				}
			}
		} catch (Exception ex) {
			Log.d(TAG, "Error while trying to get extra data", ex);
		}
		
		result = new PluginResult(Status.OK, data);
		
		return result;
	}

	private void addToAlarmManager(Context context, Alarm alarm) {
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		intent.putExtra("id", alarm.getId());
		
		PendingIntent pi = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
		
		//Since, from API level 19, set is not any longer fixed in time but is approximate and decided by Android, we use setExact for >=19 (kitkat)
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		boolean done = false;
		Log.d(TAG, " --------- API LEVEL:" +  currentapiVersion);
		if (currentapiVersion >= 19){
		    Log.d(TAG, "Using setExact()");

		    try {
				Method setExactMethod = AlarmManager.class.getMethod( "setExact", new Class[] { Integer.TYPE, Long.TYPE, PendingIntent.class } );
				if (setExactMethod == null ) {
					Log.d(TAG, "Unable to find setExact method");
				} else {
					setExactMethod.invoke(am, new Object[] { AlarmManager.RTC_WAKEUP, alarm.getWhen().getTime(), pi } );
					
					done = true;
				}
			} catch (Exception ex) {
				Log.d(TAG, "Error occurred while trying to use setExact via reflection", ex);
			}
		} 
		
		if (!done) {
		    Log.d(TAG, "Using set()");
		    // do something for phones running an SDK before Kitkat or if the above fails		    
		    am.set(AlarmManager.RTC_WAKEUP, alarm.getWhen().getTime(), pi);
		}
	}
	
	private void cancelFromAlarmManager(Context context, Alarm alarm) {
		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}
