package com.agup.gps.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class GPSAlertDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("GPS is not currently enabled. Click ok to proceed to Location Settings.")
			.setTitle("Toggle GPS");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
					Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				    getActivity().startActivity(settingsIntent);
	           }
	    });
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               GPSAlertDialogFragment.this.getDialog().cancel();
	           }
	    });		
		
		AlertDialog dialog = builder.create();		
		
		return dialog;
		
	}
	
}
