package apt.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DetailForm extends Activity {
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private EditText feed = null;
	private RadioGroup types = null;
	private RestaurantHelper helper;
	private String restaurantId = null; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);
		
		name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.address);
		notes = (EditText)findViewById(R.id.notes);
		feed = (EditText)findViewById(R.id.feed);
		types = (RadioGroup)findViewById(R.id.types);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
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
	
	 private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
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
			
			finish();			
		}
	};
	
	private void load() {
		Cursor cursor = helper.getById(restaurantId);
		
		cursor.moveToFirst();
		name.setText(helper.getName(cursor));
		address.setText(helper.getAddress(cursor));
		notes.setText(helper.getNotes(cursor));
		feed.setText(helper.getFeed(cursor));
		
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
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable() {
		return ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
	}
}
