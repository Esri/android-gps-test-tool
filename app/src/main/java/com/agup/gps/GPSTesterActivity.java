package com.agup.gps;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.agup.gps.controllers.GPSTesterActivityController;
import com.esri.android.map.MapView;

public class GPSTesterActivity extends Activity {
	
	private GPSTesterActivityController _activityController = null;
	private MapView _map; 
	private static SharedPreferences _preferences;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		_map = (MapView)findViewById(R.id.map);
		
		_activityController = new GPSTesterActivityController(this,GPSTesterActivity.this,_map);	
		
		_preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		PreferenceManager.setDefaultValues(this,R.xml.preferences , false);			
		
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

    	MenuInflater inflator = getMenuInflater();
    	inflator.inflate(R.menu.options_menu, menu);

		return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
    	if(keycode == KeyEvent.KEYCODE_MENU)
    	{
        	Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        	startActivity(intent);
    	}
    	return super.onKeyDown(keycode, event);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	switch( item.getItemId()) {
            case R.id.settings_option_item:
                _activityController.launchSettingsView();
                return true;
			case R.id.satellitedata2:
				_activityController.launchSatelliteView();
				return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override 
	protected void onDestroy() { 
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		if(_activityController != null){
			_activityController.stopLocation();
		}
		_map.pause();
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		if(_activityController != null){
			_activityController.stopLocation();
		}
	}
	
	@Override 	
	protected void onResume() {
		super.onResume(); 
		_map.unpause();
		if(_activityController != null && _preferences.getBoolean("pref_key_autostart", true)){
			_activityController.startLocation();
		}
	}

}