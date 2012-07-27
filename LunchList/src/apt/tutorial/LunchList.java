package apt.tutorial;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class LunchList extends TabActivity {
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private RadioGroup types = null;
	private RestaurantHelper helper;
	private Cursor model = null;
    private RestaurantAdapter adapter = null;
	
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
			RestaurantHolder holder= new RestaurantHolder(row);
			
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
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        helper = new RestaurantHelper(this);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.address);
		notes = (EditText)findViewById(R.id.notes);
		types = (RadioGroup)findViewById(R.id.types);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        
        model = helper.getAll();
        startManagingCursor(model);
        adapter = new RestaurantAdapter(model);
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
			
			helper.insert(name.getText().toString(), address.getText().toString(),
					type.toString(), notes.getText().toString());
			
			model.requery();
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			model.moveToPosition(position);			
			name.setText(helper.getName(model));
			address.setText(helper.getAddress(model));
			notes.setText(helper.getNotes(model));
			
			switch(helper.getType(model))
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
			
			getTabHost().setCurrentTab(1);
		}
	};
}