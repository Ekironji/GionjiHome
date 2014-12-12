package com.gionji.gionjihome.xmas;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gionji.gionjihome.GHMainActivity;
import com.gionji.gionjihome.R;

public class RelayActivity extends Activity implements OnClickListener{
	
	
	private int[] buttonsIds = {R.id.button1, R.id.button2, R.id.button3, R.id.button4};
	ArrayList<Button> buttons = new ArrayList<Button>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relay);
		
		for(int i=0; i<buttonsIds.length; i++){
			buttons.add( (Button) findViewById(buttonsIds[i]));
			buttons.get(i).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		for(int i=0; i<buttonsIds.length; i++){
			if(v.getId() == buttonsIds[i])
				GHMainActivity.mEkironjiDevice.switchRelay(i);
		}
		
	}
	
	
	

	
}
