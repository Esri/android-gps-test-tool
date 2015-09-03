package com.agup.gps.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class NetworkAlertDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Internet is not currently enabled. Click ok to proceed to Settings.")
			.setTitle("Toggle Wireless Settings");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
					Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				    getActivity().startActivity(settingsIntent);
	           }
	    });
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               NetworkAlertDialogFragment.this.getDialog().cancel();
	           }
	    });		
		
		AlertDialog dialog = builder.create();		
		
		return dialog;
		
	}
	
}
