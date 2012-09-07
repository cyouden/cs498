package apt.tutorial;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class LunchList extends TabActivity {
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private RadioGroup types = null;
	private Restaurant current = null;
	
	public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(LunchList.this, R.layout.row, model);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				
				row = inflater.inflate(R.layout.row, parent, false);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else {
				holder = (RestaurantHolder)row.getTag();
			}
			
			holder.populateFrom(model.get(position));
			
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
		
		
		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());
			
			if (r.getType() == Restaurant.Type.SIT_DOWN) {
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (r.getType() == Restaurant.Type.TAKE_OUT) {
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}
	}
	
    List<Restaurant> model = new ArrayList<Restaurant>();
    RestaurantAdapter adapter = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.address);
		notes = (EditText)findViewById(R.id.notes);
		types = (RadioGroup)findViewById(R.id.types);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        
        adapter = new RestaurantAdapter();
        
        list.setAdapter(adapter);
        
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        
        getTabHost().addTab(spec);
        
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        
        getTabHost().addTab(spec);
        
        getTabHost().setCurrentTab(0);
        
        list.setOnItemClickListener(onListClick);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
			current = new Restaurant();
			
			current.setName(name.getText().toString());
			current.setAddress(address.getText().toString());	
			current.setNotes(notes.getText().toString());
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					current.setType(Restaurant.Type.SIT_DOWN);
					break;
				case R.id.take_out:
					current.setType(Restaurant.Type.TAKE_OUT);
					break;
				case R.id.delivery:
					current.setType(Restaurant.Type.DELIVERY);
					break;
			}
			
			adapter.add(current);
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			current = model.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());
			
			switch(current.getType())
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
				default:
					break;
			}
			
			getTabHost().setCurrentTab(1);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void ErrorDialog(Exception e) {
		String message = "And error has occurred: " + e.getClass().toString() + ": " + e.getMessage();
		new AlertDialog.Builder(this).setMessage(message)
			.setPositiveButton("OK", 
				new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) { /* do nothing */ }
				}).show();
		
		Log.e("LunchList Error", message);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.toast:
				String message = "No restaurant selected";
				
				if (current != null) {
					message = current.getNotes();
				}
				
				new AlertDialog.Builder(this).setMessage(message)
					.setPositiveButton("OK", 
						new OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) { /* do nothing */ }
						}).show();
				
				return true;
				
			case R.id.switch_tabs:
				getTabHost().setCurrentTab((getTabHost().getCurrentTab() + 1) % 2);
				
				return true;
				
			case R.id.make_error:
				
				try {
					int i = 1 / 0;
				}
				catch (Exception e) {
					ErrorDialog(e);
				}
				
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}