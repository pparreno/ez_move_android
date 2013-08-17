package com.ezmove.ezmove;

import android.os.Bundle;
import android.app.Activity;import android.app.Fragment;
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
import android.content.res.Configuration;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapActivity extends Activity {

	  static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	  static final LatLng KIEL = new LatLng(53.551, 9.993);
	  private GoogleMap map;
	  
	    private DrawerLayout mDrawerLayout;
	    private ListView mDrawerListLeft, mDrawerListRight;
	    private ActionBarDrawerToggle mDrawerToggle;
	    private boolean rightToggle = false;

	    private CharSequence mDrawerTitle;
	    private CharSequence mTitle;
	    private String[] mMenuItems;
	    private String[] mTripItems;

	    private class LeftDrawerItemClickListener implements ListView.OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            selectLeftItem(position);
	        }
	    }  
	    
	    private void selectLeftItem(int position) {
	        mDrawerListLeft.setItemChecked(position, true);
	        setTitle(mMenuItems[position]);
	        mDrawerLayout.closeDrawer(mDrawerListLeft);
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
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);	
        
        if (savedInstanceState == null) {
            selectLeftItem(0);
        }
        
//	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
//	        .getMap();
//	    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
//	        .title("Hamburg"));
//	    Marker kiel = map.addMarker(new MarkerOptions()
//	        .position(KIEL)
//	        .title("Kiel")
//	        .snippet("Kiel is cool")
//	        .icon(BitmapDescriptorFactory
//	            .fromResource(R.drawable.ic_launcher)));

	    // Move the camera instantly to hamburg with a zoom of 15.
//	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

	    // Zoom in, animating the camera.
//	    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
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
	
    
    public static class NavFragment extends Fragment {
    	public static final String ARG_MENU_NUMBER = "menu_number";
    	private int nav_fragment_type;
    	
    	public NavFragment(){}
    	

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			int menu_arg = getArguments().getInt(ARG_MENU_NUMBER);
			nav_fragment_type = this.findNavView(menu_arg);
			View rootView = inflater.inflate(this.nav_fragment_type, container, false);
			return rootView;
		}
		
		private int findNavView(int menu_num)
		{
			int nav_id = -1;
			switch(menu_num)
			{
				
			}
			
			return nav_id;
		}
		
    }
    

    
}
