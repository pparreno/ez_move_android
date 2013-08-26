package com.ezmove.ezmove;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.SearchManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.PolylineOptionsCreator;

public class MainMapActivity extends Activity implements LocationListener{

	  	private GoogleMap map;
	  	private Location curLocation;
	  	private Boolean initLoc = false;
	  
	    private DrawerLayout mDrawerLayout;
	    private ListView mDrawerListLeft, mDrawerListRight;
	    private ActionBarDrawerToggle mDrawerToggle;
	    private boolean rightToggle = false;
	    private int curAct;

	    private CharSequence mDrawerTitle;
	    private CharSequence mTitle;
	    private String[] mMenuItems;
	    private String[] mTripItems;

	    private LocationManager mLocManager;
	    private String locProvider;
	    
	    private class LeftDrawerItemClickListener implements ListView.OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            System.out.println(curAct);
	        	selectLeftItem(position);
	        }
	    }  
	    
	    
	    private void selectLeftItem(int position) {
	    	Intent nextAct = null;	
	    	Boolean newAct = false;
	    	System.out.println(position);
	    	if(position != curAct){
	    		switch(position){
	    		case 0:
	    			 break;
	    		case 1:{
	    			nextAct = new Intent(this, NewTrip.class);
	    			newAct = true;
	    			break;
	    		}
	    		default:;
	    		}	
	    	}
	        if(newAct == true)
	        {
	        	finish();
	        	startActivity(nextAct);
	        }
	        else
	        {
		        mDrawerListLeft.setItemChecked(position, true);
		        setTitle(mMenuItems[position]);
		        mDrawerLayout.closeDrawer(mDrawerListLeft);
	        }
	    }
	    
	   private LatLng setLatAndLng(Location loc){
		   LatLng temp = new LatLng(loc.getLatitude(), loc.getLongitude());
		   return temp;
	   } 
	    
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main_map);
	    
	    mTitle = mDrawerTitle = getTitle();
	    mMenuItems = getResources().getStringArray(R.array.ez_move_trip_menu);
	    mTripItems = getResources().getStringArray(R.array.ez_move_current_trip);
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerListLeft = (ListView) findViewById(R.id.left_drawer);
	    mDrawerListRight = (ListView) findViewById(R.id.right_drawer);
	    
	    
	    mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria locCriteria = new Criteria();
	    locCriteria.setAccuracy(Criteria.ACCURACY_FINE);
	    locCriteria.setPowerRequirement(Criteria.POWER_LOW);
	    locProvider = mLocManager.getBestProvider(locCriteria, true);
	    curLocation = mLocManager.getLastKnownLocation(locProvider);
	    
	    
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerListLeft.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navdrawer_listitem, mMenuItems));
        mDrawerListRight.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navdrawer_listitem, mTripItems));
        mDrawerListLeft.setOnItemClickListener(new LeftDrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
	    
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	if(rightToggle == true){
            	rightToggle = false;
            	mTitle = mDrawerTitle;
            	}
            	getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	if(rightToggle == false){
            	mDrawerTitle = getResources().getString(R.string.app_name);
            	getActionBar().setTitle(mDrawerTitle);
            	}
	            curAct = mDrawerListLeft.getCheckedItemPosition();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);	
        
        if (savedInstanceState == null) {
            selectLeftItem(0);
        }
       
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
	    String url = makeURL(10.31068,123.917888, 10.318795,123.903735);
	    ConnectAsyncTask task = new ConnectAsyncTask(url);
	    task.execute();
//	    map.setMyLocationEnabled(true);
//	    map.getUiSettings().setMyLocationButtonEnabled(true);
//	    map.getUiSettings().setZoomControlsEnabled(false);
//	    if (curLocation != null) {
//	        onLocationChanged(curLocation);
//	     } 
	  }

	 
	  public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
	        StringBuilder urlString = new StringBuilder();
	        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
	        urlString.append("?origin=");// from
	        urlString.append(Double.toString(sourcelat));
	        urlString.append(",");
	        urlString
	                .append(Double.toString( sourcelog));
	        urlString.append("&destination=");// to
	        urlString
	                .append(Double.toString( destlat));
	        urlString.append(",");
	        urlString.append(Double.toString( destlog));
	        urlString.append("&sensor=false&mode=driving&alternatives=true");
	        return urlString.toString();
	 }  
	  
	  public void drawPath(String  result) {

		    try {
		            //Tranform the string into a json object
		           final JSONObject json = new JSONObject(result);
		           JSONArray routeArray = json.getJSONArray("routes");
		           JSONObject routes = routeArray.getJSONObject(0);
		           JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
		           String encodedString = overviewPolylines.getString("points");
		           List<LatLng> list = decodePoly(encodedString);

		           for(int z = 0; z<list.size()-1;z++){
		                LatLng src= list.get(z);
		                LatLng dest= list.get(z+1);
		                Polyline line = map.addPolyline(new PolylineOptions()
		                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
		                .width(2)
		                .color(Color.BLUE).geodesic(true));
		            }

		    } 
		    catch (JSONException e) {

		    }
		} 
	  
	  private List<LatLng> decodePoly(String encoded) {

		    List<LatLng> poly = new ArrayList<LatLng>();
		    int index = 0, len = encoded.length();
		    int lat = 0, lng = 0;

		    while (index < len) {
		        int b, shift = 0, result = 0;
		        do {
		            b = encoded.charAt(index++) - 63;
		            result |= (b & 0x1f) << shift;
		            shift += 5;
		        } while (b >= 0x20);
		        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		        lat += dlat;

		        shift = 0;
		        result = 0;
		        do {
		            b = encoded.charAt(index++) - 63;
		            result |= (b & 0x1f) << shift;
		            shift += 5;
		        } while (b >= 0x20);
		        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		        lng += dlng;

		        LatLng p = new LatLng( (((double) lat / 1E5)),
		                 (((double) lng / 1E5) ));
		        poly.add(p);
		    }

		    return poly;
		}
	  
	  
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mLocManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocManager.requestLocationUpdates(locProvider, (long)400, (float)1.0, this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
        if (mDrawerToggle.onOptionsItemSelected(item)) {
        	mDrawerLayout.closeDrawer(Gravity.END);
            return true;
        }
        else if(item.getItemId() == R.id.right_navdrawer){
        	if(mDrawerLayout.isDrawerOpen(Gravity.END) == true){
        		rightToggle = false;
        		mTitle = mDrawerTitle;
        		mDrawerLayout.closeDrawer(Gravity.END);
        	}
        	else{
        		rightToggle = true;
        		mDrawerTitle = mTitle;
        		mTitle = getResources().getString(R.string.trip_details);
        		mDrawerLayout.closeDrawer(Gravity.START);
        		mDrawerLayout.openDrawer(Gravity.END);
        		}
        	getActionBar().setTitle(mTitle);
        }
		return super.onOptionsItemSelected(item);
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_map, menu);
		
	/*	SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName())); */
		return true; 
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListLeft);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		LatLng temp = setLatAndLng(location);
//		map.addMarker(new MarkerOptions()
//			.position(temp)
//			.title("You Are Here"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, (float)15));
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
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
    
	private class ConnectAsyncTask extends AsyncTask<Void, Void, String>{
	    private ProgressDialog progressDialog;
	    String url;
	    ConnectAsyncTask(String urlPass){
	        url = urlPass;
	    }
	    @Override
	    protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(MainMapActivity.this);
	        progressDialog.setMessage("Fetching route, Please wait...");
	        progressDialog.setIndeterminate(true);
	        progressDialog.show();
	    }
	    @Override
	    protected String doInBackground(Void... params) {
	        JSONParser jParser = new JSONParser();
	        String json = jParser.getJSONFromUrl(url);
	        return json;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);   
	        progressDialog.hide();        
	        if(result!=null){
	            drawPath(result);
	            map.addMarker(new MarkerOptions().position(new LatLng(10.31068,123.917888))
	               .title("Source"));
	            map.addMarker(new MarkerOptions().position(new LatLng(10.318795,123.903735))
	 	               .title("Destination"));
	            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.31068,123.917888), (float)13));
	        }
	    }
	}
    
}
