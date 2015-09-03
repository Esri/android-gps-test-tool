package com.agup.gps.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckConnectivity {
	   	static ConnectivityManager connectivityManager;
	    static NetworkInfo wifiInfo;
		static NetworkInfo mobileInfo;
		static NetworkInfo network;
	 
	    /**
	     * Check for <code>TYPE_WIFI</code> and <code>TYPE_MOBILE</code> connection using <code>isConnected()</code>
	     * Checks for generic Exceptions and writes them to logcat as <code>CheckConnectivity Exception</code>. 
	     * Make sure AndroidManifest.xml has appropriate permissions.
	     * @param con Application context
	     * @return Boolean
	     */
	    public static Boolean checkNow(Context con){
	         
	        try{
	            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
	            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   
	            network = connectivityManager.getActiveNetworkInfo();
	            
	            if(wifiInfo.isConnected() || mobileInfo.isConnected() || network.isConnected())
	            {
	                return true;
	            }
	        }
	        catch(Exception e){
	            Log.d("GPSTester","CheckConnectivity Exception: " + e.getMessage());
	        }
	         
	        return false;
	    }   
}
