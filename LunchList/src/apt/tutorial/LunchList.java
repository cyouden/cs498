package apt.tutorial;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class LunchList extends TabActivity {
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	
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
			Restaurant r = new Restaurant();
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());		
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					r.setType(Restaurant.Type.SIT_DOWN);
					break;
				case R.id.take_out:
					r.setType(Restaurant.Type.TAKE_OUT);
					break;
				case R.id.delivery:
					r.setType(Restaurant.Type.DELIVERY);
					break;
			}
			
			adapter.add(r);
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Restaurant r = model.get(position);
			
			name.setText(r.getName());
			address.setText(r.getAddress());
			
			switch(r.getType())
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
}