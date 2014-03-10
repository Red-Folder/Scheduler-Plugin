package com.red_folder.phonegap.plugin.scheduler.models;

public enum ShowOption {
	None,
	WakeScreen,
	ShowOverLockScreen;
	
	
	private static String NONE = "None";
	private static String WAKE_SCREEN = "WakeScreen";
	private static String SHOW_OVER_LOCK_SCREEN = "ShowOverLockScreen";
	
	public static ShowOption parse(String option) {
		if (WAKE_SCREEN.toLowerCase().equals(option.toLowerCase()))
			return ShowOption.WakeScreen;
		
		if (SHOW_OVER_LOCK_SCREEN.toLowerCase().equals(option.toLowerCase()))
			return ShowOption.ShowOverLockScreen;
		
		return ShowOption.None;
	}
	
	@Override
	public String toString() {
		switch (this) {
			case WakeScreen: return WAKE_SCREEN;
			case ShowOverLockScreen: return SHOW_OVER_LOCK_SCREEN;
			default: return NONE;
		}
	}
}
