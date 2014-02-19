package com.red_folder.phonegap.plugin.scheduler.models;


import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AlarmDictionary extends HashMap<Integer, Alarm> {
	
	private static final String TAG = AlarmDictionary.class.getSimpleName();

	/**
	 * 
	 */
	private static final long serialVersionUID = 123995933183573153L;

	public JSONArray toJSON() {
		JSONArray result = new JSONArray();
		
		for (Integer key : this.keySet()) {
			Alarm alarm = this.get(key);
			JSONObject jsonObject = alarm.toJSON();
			if (jsonObject != null)
				result.put(jsonObject);
		}
		
		return result;
	}
	
	public static AlarmDictionary fromJSON(JSONArray array) {
		AlarmDictionary result = new AlarmDictionary();
		
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				Alarm tmpAlarm = null;

				try { tmpAlarm = Alarm.fromJSON(array.getJSONObject(i)); } catch (JSONException ex) { Log.d(TAG, "Unable to get JSONObject from array", ex); }

				if (tmpAlarm != null)
					result.put(tmpAlarm.getId(), tmpAlarm);
			}
		}
		
		return result;
	}
	
	public Integer newID() {
		Integer max = 0;
		
		for (Integer key : this.keySet()) {
			if (max < key)
				max = key;
		}
		
		return max + 1;
	}
}
