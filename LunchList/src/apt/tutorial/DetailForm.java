package apt.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailForm extends Activity {
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private EditText feed = null;
	private RadioGroup types = null;
	private TextView location = null;
	private RestaurantHelper helper;
	private String restaurantId = null; 
	private LocationManager locMgr = null;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);
		locMgr = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.address);
		notes = (EditText)findViewById(R.id.notes);
		feed = (EditText)findViewById(R.id.feed);
		types = (RadioGroup)findViewById(R.id.types);
		location = (TextView)findViewById(R.id.location);
        
        restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
		
		if (restaurantId != null) {
			load();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	@Override
	public void onPause() {
		save();
		locMgr.removeUpdates(onLocationChange);
		
		super.onPause();
	}
	
	private void save() {
		Restaurant.Type type = Restaurant.Type.NULL;
		
		switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				type = Restaurant.Type.SIT_DOWN;
				break;
			case R.id.take_out:
				type = Restaurant.Type.TAKE_OUT;
				break;
			case R.id.delivery:
				type = Restaurant.Type.DELIVERY;
				break;
		}
		
		if (restaurantId == null) {
			helper.insert(
					name.getText().toString(), 
					address.getText().toString(),
					type.toString(), 
					notes.getText().toString(),
					feed.getText().toString());
		}
		else {
			helper.update(
					restaurantId,
					name.getText().toString(), 
					address.getText().toString(),
					type.toString(), 
					notes.getText().toString(),
					feed.getText().toString());
		}		
	}
	
	private void load() {
		Cursor cursor = helper.getById(restaurantId);
		
		cursor.moveToFirst();
		name.setText(helper.getName(cursor));
		address.setText(helper.getAddress(cursor));
		notes.setText(helper.getNotes(cursor));
		feed.setText(helper.getFeed(cursor));
		location.setText(String.valueOf(helper.getLatitude(cursor)) + ", " + String.valueOf(helper.getLongitude(cursor)));
		
		switch(helper.getType(cursor))
		{
			case SIT_DOWN:
				types.check(R.id.sit_down);
				break;
			case TAKE_OUT:
				types.check(R.id.take_out);
				break;
			case DELIVERY:
				types.check(R.id.delivery);
				break;
		}
		
		cursor.close();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		
		name.setText(state.getString("name"));
		address.setText(state.getString("address"));
		notes.setText(state.getString("notes"));
		types.check(state.getInt("type"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_option, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.feed:
				if (isNetworkAvailable()) {
					Intent intent = new Intent(this, FeedActivity.class);
					intent.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
					startActivity(intent);
				}
				else {
					Toast.makeText(this, "Sorry, network connection is not available", Toast.LENGTH_LONG).show();
				}
				
				return true;
			case R.id.location:
				locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
				
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable() {
		return ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
	}
	
	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location fix) {
			helper.updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
			
			location.setText(String.valueOf(fix.getLatitude()) + ", " + String.valueOf(fix.getLongitude()));
			
			locMgr.removeUpdates(onLocationChange);
			
			Toast.makeText(DetailForm.this, "Location Saved", Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String provider) {
			//unused
		}

		public void onProviderEnabled(String provider) {
			//unused
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			//unused
		}
	};
}
