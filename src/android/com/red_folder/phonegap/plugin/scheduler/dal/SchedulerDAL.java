package com.red_folder.phonegap.plugin.scheduler.dal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.red_folder.phonegap.helpers.PreferencesHelper;
import com.red_folder.phonegap.plugin.scheduler.models.Alarm;
import com.red_folder.phonegap.plugin.scheduler.models.AlarmDictionary;

public class SchedulerDAL {
	
	private static final String TAG = SchedulerDAL.class.getSimpleName();
	private static final String DATA_KEY = "com.red_folder.phonegap.plugin.scheduler.dal.SchedulerDAL.data";

	private Context mContext;
	private AlarmDictionary mDictionary;
	
	public SchedulerDAL(Context context) {
		this.mContext = context;
		
		load();
	}
	
	public boolean exists(Alarm alarm) {
		return this.mDictionary.containsKey(alarm.getId());
	}
	
	public void load() {
		String storedData = PreferencesHelper.get(this.mContext, DATA_KEY);
		JSONArray json = null;
		
		if (storedData.length() > 0) 
			try { json = new JSONArray(storedData);	} catch (JSONException ex) { Log.d(TAG, "Unable to convert storedData to JSONArray", ex); }
		
		this.mDictionary = AlarmDictionary.fromJSON(json);
	}
	
	public void save() {
		PreferencesHelper.set(this.mContext, DATA_KEY, this.mDictionary.toJSON().toString());
	}
	
	public int add(Alarm alarm) {
		alarm.setId(this.mDictionary.newID());
		this.mDictionary.put(alarm.getId(), alarm);
		return alarm.getId();
	}
	
	public boolean update(Alarm alarm) {
		Boolean result = false;
		
		if (alarm != null) {
			if (alarm.getId() > 0) {
				if (this.mDictionary.containsKey(alarm.getId())) {
					if (this.delete(alarm)) {
						this.mDictionary.put(alarm.getId(), alarm);
					
						result = true;
					}
				}
			}
		}
		
		return result;
	}
	
	public boolean delete(Alarm alarm) {
		Boolean result = false;
		
		if (alarm != null) {
			if (alarm.getId() > 0) {
				if (this.mDictionary.containsKey(alarm.getId())) {
					this.mDictionary.remove(alarm.getId());
					
					result = true;
				}
			}
		}
		
		return result;
	}
	
	public Alarm get(Integer id) {
		return this.mDictionary.get(id);
	}

	public JSONObject getAsJSON(Integer id) {
		Alarm alarm = this.mDictionary.get(id);
		
		if (alarm == null)
			return null;
		else
			return alarm.toJSON();
	}

	public JSONArray getAsJSON() {
		return this.mDictionary.toJSON();
	}


}
