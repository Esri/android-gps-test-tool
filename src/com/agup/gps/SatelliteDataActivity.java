package com.agup.gps;

import com.agup.gps.controllers.SatelliteDataActivityController;

import android.app.Activity;
import android.os.Bundle;

public class SatelliteDataActivity extends Activity {
    
	private SatelliteDataActivityController _activityController = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satdata);
        
        _activityController = new SatelliteDataActivityController(this);    }
	
	@Override 
	protected void onDestroy() { 
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		_activityController.stopLocation();
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		_activityController.stopLocation();		
		super.onStop();
	}
	
	@Override 	
	protected void onResume() {
		super.onResume(); 
		if(_activityController != null){
			_activityController.startLocation();
		}
	}	
}
