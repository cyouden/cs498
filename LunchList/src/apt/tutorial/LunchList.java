package apt.tutorial;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LunchList extends ListActivity {
	public final static String ID_EXTRA = "apt.tutorial._ID";
	
	private RestaurantHelper helper;
	private Cursor model = null;
    private RestaurantAdapter adapter = null;
    private SharedPreferences prefs;
	
	public class RestaurantAdapter extends CursorAdapter {
		RestaurantAdapter(Cursor c) {
			super(LunchList.this, c);
		}

		@Override
		public void bindView(View row, Context context, Cursor cursor) {
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			holder.populateFrom(cursor, helper);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			
			row.setTag(holder);
			
			return row;
		}
	}
		
	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;
				
		RestaurantHolder(View row) {
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address_row_view);
			icon = (ImageView)row.findViewById(R.id.icon);
		}
		
		
		void populateFrom(Cursor cursor, RestaurantHelper helper) {
			name.setText(helper.getName(cursor));
			address.setText(helper.getAddress(cursor));
			
			switch (helper.getType(cursor)) {
				case SIT_DOWN:
					icon.setImageResource(R.drawable.ball_red);
					break;
				case TAKE_OUT:
					icon.setImageResource(R.drawable.ball_yellow);
					break;
				case DELIVERY:
					icon.setImageResource(R.drawable.ball_green);
					break;
				default:
					break;
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        helper = new RestaurantHelper(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        initList();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }
    
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener =
		new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				if (key.equals("sort_order")) {
					initList();
				}
			}
		};
		
	private void initList() {
		if (model != null) {
			stopManagingCursor(model);
			model.close();
		}
		
		model = helper.getAll(prefs.getString("sort_order", "name"));
		startManagingCursor(model);
		adapter = new RestaurantAdapter(model);
		setListAdapter(adapter);
	}
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	helper.close();
    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
    	Intent intent = new Intent(LunchList.this, DetailForm.class);
		
    	intent.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.option, menu);
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.add:
    			startActivity(new Intent(LunchList.this, DetailForm.class));
    			return true;
    		case R.id.prefs:
    			startActivity(new Intent(this, EditPreferences.class));
    			return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
}