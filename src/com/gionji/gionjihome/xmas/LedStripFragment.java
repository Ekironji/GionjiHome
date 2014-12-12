package com.gionji.gionjihome.xmas;

import java.util.ArrayList;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.gionji.gionjihome.GHMainActivity;
import com.gionji.gionjihome.R;
import com.gionji.gionjihome.xmas.HSVColorPickerDialog.OnColorSelectedListener;

public class LedStripFragment extends Fragment implements OnClickListener {

	LedActivity mainActivity = null;
	int stripId = 0;
	
	int buttonIds[] = {R.id.chooseColorButton, R.id.turnOffButton, 
			R.id.blinkButton, R.id.rainbowButton, R.id.setButton};
	
	ArrayList<Button> buttons = null;
	
	public LedStripFragment(int stripId) {
		this.stripId = stripId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
		super.onCreateView(inflater, container, savedInstanceState);
		mainActivity = (LedActivity) getActivity();
  
	    View view = inflater.inflate(R.layout.fragment_ledstrip, container, false);
	    
	    buttons = new ArrayList<Button>();
	    
	    for(int i=0; i<buttonIds.length; i++){
	    	buttons.add((Button) view.findViewById(buttonIds[i]));
	    	buttons.get(i).setOnClickListener(this);
	    }
	    
	    return view;
	}
	
	
	HSVColorPickerDialog cpd;
	private int lastColorSelected = Color.GREEN;
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.chooseColorButton:
			cpd = new HSVColorPickerDialog(mainActivity, lastColorSelected, new OnColorSelectedListener() {
			    @Override
			    public void colorSelected(Integer color) {
			    	lastColorSelected = color;
					Log.i("ScanViewFragmanet.onClick()", "rgb " + lastColorSelected);	
					buttons.get(0).setBackgroundColor(lastColorSelected + 0xbb000000);
			    }
			});
			cpd.setTitle( "Pick a color" );
			cpd.show();
			break;
		case R.id.turnOffButton:
			GHMainActivity.mEkironjiDevice.sendSimpleColor(stripId, Color.BLACK);
			break;
		case R.id.blinkButton:
			GHMainActivity.mEkironjiDevice.sendBlinkColor(stripId, lastColorSelected);
			break;
		case R.id.rainbowButton:
			GHMainActivity.mEkironjiDevice.sendRainbowColor(stripId);
			break;
		case R.id.setButton:
			GHMainActivity.mEkironjiDevice.sendSimpleColor(stripId, lastColorSelected);
			break;
		}
	}

}
