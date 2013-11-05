package com.agup.gps.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {
	
	private static SharedPreferences _settings;
	
	public static boolean setSharedPreferences(PreferenceKey key, String value, Activity activity){
		
		boolean commit = false;
		
		_settings = activity.getPreferences(Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = _settings.edit();
		editor.putString(key.toString(), value);
		commit = editor.commit();
		
		return commit;
		
	}
	
	public static String getSharedPreferences(PreferenceKey key, Activity activity){
		
		String result = null;		
		_settings = activity.getPreferences(Context.MODE_PRIVATE);		
		result = _settings.getString(key.toString(), null);
		
		return result;
	}	
	
	public enum PreferenceKey{
		GPS_LATLON("gps_latlon"), 
		NETWORK_LATLON("network_latlon"), 
		CACHED_NETWORK_LATLON("cached_network_latlon"), 
		CACHED_GPS_LATLON("cached_gps_latlon"), 
		PASSIVE_LATLON("passive_latlon");
		
		private String value;
		private PreferenceKey(String value){
			this.value = value;
		}
		
		/**
		 * Allows you to retrieve the value for each PreferenceKey enum
		 * @return value String
		 */
		public String toString(){
			return value;
		}
	}
}
