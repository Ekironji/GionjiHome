/**
*  Copyright (C) 2014 Ekironji <ekironjisolutions@gmail.com>
*
*  This file is part of UdooLights
*
*  UdooLights is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  UdooLights is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.udoo.xmas.activities;

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

import org.udoo.xmas.R;
import org.udoo.xmas.UMainActivity;
import org.udoo.xmas.activities.HSVColorPickerDialog.OnColorSelectedListener;


public class LedStripFragment extends Fragment implements OnClickListener {

	// Main Activity reference
	LedActivity mainActivity = null;
	
	// Led Tab id
	int stripId = 0;
	
	// View reference
	int buttonIds[] = {R.id.chooseColorButton, 
			R.id.turnOffButton, 
			R.id.blinkButton, 
			R.id.rainbowButton, 
			R.id.setButton};
	
	// ArrayList containing buttons objects
	ArrayList<Button> buttons = null;
	
	
	// Constructor
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
	    
	    // Getting reference to buttons and set onClickListener one or each buttons
	    for(int i=0; i<buttonIds.length; i++){
	    	buttons.add((Button) view.findViewById(buttonIds[i]));
	    	buttons.get(i).setOnClickListener(this);
	    }
	    
	    return view;
	}
	
	// color picker alert object
	private HSVColorPickerDialog cpd;
	// selected color
	private int lastColorSelected = Color.MAGENTA;
	
	// OnClickListener 
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
		case R.id.setButton:
			UMainActivity.mEkironjiDevice.sendSimpleColor(stripId, lastColorSelected);
			break;
		case R.id.blinkButton:
			UMainActivity.mEkironjiDevice.sendBlinkColor(stripId, lastColorSelected);
			break;
		case R.id.rainbowButton:
			UMainActivity.mEkironjiDevice.sendRainbowColor(stripId);
			break;
		case R.id.turnOffButton:
			UMainActivity.mEkironjiDevice.sendSimpleColor(stripId, Color.BLACK);
			break;
		}
	}

}
