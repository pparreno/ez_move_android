package com.ezmove.ezmove;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class NewTrip extends Activity {
	
	private GoogleMap map;
	private SelectDialog selDialog;
	private Button manmode, automode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_trip);
		
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.newtrip_map))
		        .getMap();
	    map.setMyLocationEnabled(true);
	    selDialog = new SelectDialog();
	    selDialog.show(getFragmentManager(), "Select Mode");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(this, MainMapActivity.class);
		finish();
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_trip, menu);
		return true;
	}

	
	
}
