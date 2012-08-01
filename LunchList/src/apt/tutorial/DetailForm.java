package apt.tutorial;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
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
						notes.getText().toString());
			}
			else {
				helper.update(
						restaurantId,
						name.getText().toString(), 
						address.getText().toString(),
						type.toString(), 
						notes.getText().toString());
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
}
