package com.red_folder.phonegap.plugin.scheduler.models;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.red_folder.phonegap.helpers.Conversion;

import android.util.Log;

public class Alarm {
	
	private static final String TAG = Alarm.class.getSimpleName();

	private int mId = -1;
	private String mClassName = null;
	private Date mWhen = null;
	private ShowOption mShowOption = ShowOption.None;
	private JSONObject mExtras = new JSONObject();
	
	public int getId() {
		return this.mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public String getClassName() {
		return this.mClassName;
	}
	
	public void setClassName(String className) {
		this.mClassName = className;
	}
	
	public Date getWhen() {
		return this.mWhen;
	}

	public void setWhen(Date when) {
		this.mWhen = when;
	}
	
	public ShowOption getShowOption() {
		return this.mShowOption;
	}
	
	public void setShowOption(ShowOption showOption) {
		this.mShowOption = showOption;
	}
	
	public JSONObject getExtras() {
		return this.mExtras;
	}
	
	public void setExtras(JSONObject extras) {
		this.mExtras = extras;
	}
	
	public JSONObject toJSON() {
		JSONObject result = new JSONObject();
		
		try { result.put("id", this.getId()); } catch (JSONException ex) { Log.d(TAG, "Adding Id to JSONObject failed", ex); }
		try { result.put("classname", this.getClassName()); } catch (JSONException ex) { Log.d(TAG, "Adding ClassName to JSONObject failed", ex); }
		try { result.put("when", Conversion.convertDateToJavaScriptDate(this.getWhen())); } catch (JSONException ex) { Log.d(TAG, "Adding When to JSONObject failed", ex); }
		try { result.put("showoption", this.getShowOption()); } catch (JSONException ex) { Log.d(TAG, "Adding ShowOption to JSONObject failed", ex); }
		try { result.put("extras", this.getExtras()); } catch (JSONException ex) { Log.d(TAG, "Adding Extras to JSONObject failed", ex); }
		
		return result;
	}

	public static Alarm fromJSON(JSONObject data) {
		Alarm result = new Alarm();
		
		if (data.has("id"))
			result.setId(data.optInt("id", -1));
		
		if (data.has("classname"))
			result.setClassName(data.optString("classname", null));
		
		if (data.has("when")) {
			Date tmpDate = Conversion.convertJavaScriptDateToDate(data.optString("when", ""));
			if (tmpDate != null)
				result.setWhen(tmpDate);
		}
		
		if (data.has("showoption"))
			result.setShowOption(ShowOption.parse(data.optString("showoption", "")));
		
		if (data.has("extras"))
			try {
				result.setExtras(new JSONObject(data.optString("extras","{}")));
			} catch (JSONException ex) {
				Log.d(TAG, "Error retrieving extras", ex);
			}

		return result;
	}
}
