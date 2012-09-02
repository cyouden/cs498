package apt.tutorial;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LunchList extends Activity {
    Restaurant r = new Restaurant();
    Random rng = new Random();
    
    private int sitDownId;
    private int takeOutId;
    private int deliveryId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        RadioGroup rg = (RadioGroup)findViewById(R.id.types);
        
        sitDownId = createRadioButton(rg, "Sit Down");
        takeOutId = createRadioButton(rg, "Take Out");
        deliveryId = createRadioButton(rg, "Delivery");
        
        for (int i = 0; i < 10; ++i) {
        	createRadioButton(rg, "Extra " + String.valueOf(i));
        }
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
    }
    
    private int createRadioButton(RadioGroup rg, String text) {
    	int id = rng.nextInt(); // May not be the safest way...but it should work for this sample with high probability
    	
    	RadioButton rb = new RadioButton(this);
    	rb.setId(id);
    	rb.setText(text);
    	
    	rg.addView(rb);
    	
    	return id;
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.address);
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());		
			
			int selectedId = types.getCheckedRadioButtonId();
			
			if (selectedId == sitDownId) {
					r.setType("sit_down");
			}
			else if (selectedId == takeOutId) {
					r.setType("sit_down");
			}
			else if (selectedId == deliveryId) {
					r.setType("sit_down");
			}
		}
	};
}