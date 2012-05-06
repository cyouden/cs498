package apt.tutorial;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

public class LunchList extends Activity {
    List<Restaurant> model = new ArrayList<Restaurant>();
    ArrayAdapter<Restaurant> adapter = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        
        adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, model);
        
        list.setAdapter(adapter);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.address);
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());		
			
			//TODO: this should really be an enum
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					r.setType("sit_down");
				case R.id.take_out:
					r.setType("sit_down");
				case R.id.delivery:
					r.setType("sit_down");
			}
			
			adapter.add(r);
		}
	};
}