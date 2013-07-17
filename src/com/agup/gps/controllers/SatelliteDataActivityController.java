package com.agup.gps.controllers;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.agup.gps.GPSTesterActivity;
import com.agup.gps.R;
import com.agup.gps.R.id;
import com.agup.gps.utils.ElapsedTimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * NOTE: Listening to GPS status and NMEA is very resource intensive. That's why
 * this has it's own dedicated view. Related to this, I've noticed delays when 
 * switching back to the main view.
 * 
 * @author Andy Gup
 *
 */
public class SatelliteDataActivityController {
	private static LocationListener _locationListenerGPSProvider = null;
	private static LocationManager _locationManager = null;
	private static Activity _activity = null;
	
	private static ElapsedTimer _elapsedTimer;	
	
	private static TextView _gpsSatelliteTextView;
	private TextView _gpsNMEATextView;
	
	private static SharedPreferences _preferences;	
	
	private ImageView _imMainActivity;	
	
	private static Button _startButton;	
	private static Button _pauseButton;
	private static Button _emailButton;
	
	private String _satelliteList = "";	
	private String _gpsNMEAText = "";
	
	private static NmeaListener _nmeaListener = null;
	private static GpsStatus.Listener _gpsStatusListener = null;
	
	private static Iterable<GpsSatellite> _satellites = null;	
	
	private final static String _format = String.format(Locale.getDefault(),"%%0%dd", 2); 		
	
	public SatelliteDataActivityController(Activity activity){
		_activity = activity;
		_elapsedTimer = new ElapsedTimer();		
		_gpsNMEATextView = (TextView) _activity.findViewById(R.id.gpsNMEAInfo);
		_gpsSatelliteTextView = (TextView) _activity.findViewById(R.id.gpsSatelliteInfo);	
		_preferences = PreferenceManager.getDefaultSharedPreferences(_activity);		
		_imMainActivity = (ImageView) _activity.findViewById(R.id.backToMainActivity);		
		_startButton = (Button)_activity.findViewById(R.id.StartSatButton);		
		_pauseButton = (Button) _activity.findViewById(R.id.PauseSatButton);		
		_emailButton = (Button) _activity.findViewById(R.id.EmailSatButton);
		
		setUI();
	}
	
	private void setUI(){
		
		//Keep screen awake
		_activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		String gpsNMEAText = "<b><font color='yellow'>GPS NMEA</b></font>" + 
				"<br><b>Timestamp:</b> N/A" + 
				"<br><b>NMEA code:</b> N/A";			

		String gpsSatelliteText = "<b><font color='yellow'>GPS Satellite</b></font>" + 
				"<br><b>Number of Satellites:</b> N/A" + 
				"<br><b>Satellite 0:</b> N/A" +
				"<br><b>Satellite 1:</b> N/A";	
		
		_gpsNMEATextView.setText(Html.fromHtml(gpsNMEAText));
		_gpsSatelliteTextView.setText(Html.fromHtml(gpsSatelliteText));
		
		_imMainActivity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(_activity.getApplicationContext(),GPSTesterActivity.class);
				_activity.startActivity(i);
			}
		});		
		
		_startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(_locationManager == null){
					startLocation();
				}
				else{
					stopLocation();
				}
			}
		});	
		
		_pauseButton.setOnClickListener(new View.OnClickListener() {			
			
			@Override
			public void onClick(View v) {				
				pauseLocation();
			}
		});	
		
		_emailButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				pauseLocation();
				
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Satellite Data");
				shareIntent.setType("plain/text"); 
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, _satelliteList + 
						"<br><br>" + _gpsNMEAText);
				
				_activity.startActivity(shareIntent);   
			}
		});
		
	}	

	private void setLocationListenerGPSProvider(){
		_locationListenerGPSProvider = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		_nmeaListener = new GpsStatus.NmeaListener() {
			
			@Override
			public void onNmeaReceived(long timestamp, String nmea) {
				String time = _elapsedTimer.convertMillisToMDYHMSS(timestamp);
				_gpsNMEAText = "<b><font color='yellow'>GPS NMEA</b></font>" +
						"<br><b>Timestamp:</b> " + time +
						"<br><b>NMEA code:</b> " + nmea;
				
				_gpsNMEATextView.setText(Html.fromHtml(_gpsNMEAText));
			}
		};
		
		_locationManager.addNmeaListener(_nmeaListener);
		

		_gpsStatusListener = new GpsStatus.Listener() {

			String seconds;
			String minutes;
			String hours;
			String ms;
			String satelliteHMS;
			String usedInFix = "false";			
			int t;			
			
			@Override
			public void onGpsStatusChanged(int event) {
				
				_satelliteList = "";
				satelliteHMS = "N/A";					
				
				//Occasionally there may be null values if GPS hiccups
				try{
					t = _locationManager.getGpsStatus(null).getTimeToFirstFix();
					//String seconds = String.format(_format, t/1000 % 60);
					seconds = String.format(_format, TimeUnit.MILLISECONDS.toSeconds(t));
					minutes = String.format(_format, TimeUnit.MILLISECONDS.toMinutes(t));
					hours = String.format(_format, TimeUnit.MILLISECONDS.toHours(t));
					ms = String.format(_format,  t % 1000);
					
					satelliteHMS = hours + ":" + minutes + ":" + seconds + ":" + ms;
					
					_satellites =  _locationManager.getGpsStatus(null).getSatellites();
					
					if(_satellites != null){
						
						for(GpsSatellite sat: _satellites ){
							
							if(sat.usedInFix() == true){
								usedInFix = "<font color='red'>true</font>";
							}
							else{
								usedInFix = "false";
							}
							
							_satelliteList = _satelliteList + "<br>" +
									sat.getPrn() + ", " +
									sat.getSnr() + ", " +
									usedInFix;
						}				
					}
				}
				catch(Exception exc){
					Log.d("GPSTester", "GPS Status error (onGpsStatusChanged): " + exc.getMessage());
				}
							
				
				if( _satelliteList != ""){
					_gpsSatelliteTextView.setText(Html.fromHtml(
							"<b><font color='yellow'>GPS Satellite Info (No., SNR, Used in fix)</b></font>" +
							"<br><b>Time to 1st fix:</b> " + satelliteHMS +
							_satelliteList));
				}	
			}
			
		};
		
		_locationManager.addGpsStatusListener(_gpsStatusListener);
		
		try{
			long minDistance = Long.valueOf(_preferences.getString("pref_key_updateGPSMinDistance", "0"));
			long minTime = Long.valueOf(_preferences.getString("pref_key_updateGPSMinTime", "0"));
		
			// Register the listener with the Location Manager to receive location updates
			_locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, minTime, minDistance, _locationListenerGPSProvider);
		
		}
		catch(Exception exc){
			Log.d("GPSTester", "Unable to start GPS provider. Bad value. " + exc.getMessage());
		}		
		
	}	
	
	public void startLocation(){		
		
		if(_locationManager == null){
			
			if(_startButton != null){
				_startButton.setTextColor(Color.RED);
				_startButton.setText("Stop");
			}			
			
			_locationManager = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
			Boolean gpsProviderEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);			
			
			if(gpsProviderEnabled){
				setLocationListenerGPSProvider();	
			}
		}
		else{
			Log.d("GPSTester","GPS Provider not enabled. Unable to set location listener.");
		}
	}
	
	public void stopLocation(){
		if(_locationManager != null){		

			
			if(_startButton != null){
				_startButton.setTextColor(Color.WHITE);
				_startButton.setText("Start");
			}			

			if(_nmeaListener != null){
				_locationManager.removeNmeaListener(_nmeaListener);
				_nmeaListener = null;
			}			
			
			if(_gpsStatusListener != null){
				_locationManager.removeGpsStatusListener(_gpsStatusListener);
				_gpsStatusListener = null;
			}			
			
			if(_locationListenerGPSProvider != null){
				_locationManager.removeUpdates(_locationListenerGPSProvider);
				_locationManager = null;				
			}
		}
		
		if(_locationListenerGPSProvider != null) {
			_locationListenerGPSProvider = null;
		}
	}
	
	public void pauseLocation(){
		String startButtonText = _startButton.getText().toString();
		if(_locationManager != null && startButtonText == "Stop"){
			_pauseButton.setTextColor(Color.RED); 
			
			if(_locationListenerGPSProvider != null){
				_locationManager.removeUpdates(_locationListenerGPSProvider);
				_locationManager = null;				
			}
			
			if(_locationListenerGPSProvider != null) {
				_locationListenerGPSProvider = null;
			}
			
			if(_gpsStatusListener != null){
				_gpsStatusListener = null;
			}
			
			if(_nmeaListener != null){
				_nmeaListener = null;
			}			
		}
		else if(_locationManager == null && startButtonText == "Stop"){
			_pauseButton.setTextColor(Color.WHITE);
			_elapsedTimer.unpauseTimer();
			_locationManager = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
			
			Boolean gpsProviderEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
							
			if(gpsProviderEnabled == true && _preferences.getBoolean("pref_key_gps", true) == true){
				setLocationListenerGPSProvider();								
			}
			else{
				Log.d("GPSTester","GPS Provider not enabled. Unable to set location listener.");				
			}
			
			gpsProviderEnabled = null;
		}
	}
}
