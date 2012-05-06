package apt.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class LunchList extends Activity {
    Restaurant r = new Restaurant();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
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
		}
	};
}