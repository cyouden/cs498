package apt.tutorial;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class LunchList extends Activity {
    List<Restaurant> model = new ArrayList<Restaurant>();
    ArrayAdapter<Restaurant> adapter = null;
    
    List<String> addressAutoComplete = new ArrayList<String>();
    ArrayAdapter<String> addressAutoCompleteAdapter = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
        Spinner list = (Spinner)findViewById(R.id.restaurants);
        
        adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_spinner_item, model);
        
        list.setAdapter(adapter);
        
        AutoCompleteTextView addr = (AutoCompleteTextView)findViewById(R.id.address);
        addressAutoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addressAutoComplete);
        
        addr.setAdapter(addressAutoCompleteAdapter);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.address);
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());	
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					r.setType("sit_down");
				case R.id.take_out:
					r.setType("sit_down");
				case R.id.delivery:
					r.setType("sit_down");
			}
			
			adapter.add(r);
			
			addressAutoCompleteAdapter.add(r.getAddress());
		}
	};
}