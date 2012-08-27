package cyouden.csci498.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Date;

public class Now extends Activity implements View.OnClickListener {
	Button btn;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		btn = new Button(this);
		btn.setOnClickListener(this);
		updateTime();
		setContentView(btn);
	}
	
	public void onClick(View view) {
		updateTime();
	}
	
	private void updateTime() {
		btn.setText(new Date().toString());
	}
}