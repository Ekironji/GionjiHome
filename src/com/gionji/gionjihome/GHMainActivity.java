package com.gionji.gionjihome;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GHMainActivity extends Activity implements OnClickListener{

	ImageButton[] mImageButtons = new ImageButton[3];
	
	private boolean[] relays = {false, false, false};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ghmain);
		
		mImageButtons[0] = (ImageButton) findViewById(R.id.imageButton1);
		mImageButtons[1] = (ImageButton) findViewById(R.id.imageButton2);
		mImageButtons[2] = (ImageButton) findViewById(R.id.imageButton3);
		
		mImageButtons[0].setOnClickListener(this);
		mImageButtons[1].setOnClickListener(this);
		mImageButtons[2].setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ghmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imageButton1:			
			break;
		case R.id.imageButton2:
			break;
		case R.id.imageButton3:
			break;
		}
	}
	
	private void switchRelay(int i){
		relays[i] = !relays[i];
		if(relays[i])
			mImageButtons[0].setAlpha(1.0f);		
		else
			mImageButtons[0].setAlpha(0.4f);
		
	}
}
