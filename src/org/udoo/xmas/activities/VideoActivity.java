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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import org.udoo.xmas.R;
import org.udoo.xmas.UMainActivity;

public class VideoActivity extends Activity implements OnClickListener{
		
	private int[] buttonsIds = {R.id.button1, R.id.button2, R.id.button3};
	private int[] imageButtonsIds = {R.id.imageButton1, R.id.imageButton2, R.id.imageButton3};
	
	ArrayList<Button> buttons = new ArrayList<Button>();	
	ArrayList<ImageButton> imageButtons = new ArrayList<ImageButton>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		for(int i=0; i<imageButtonsIds.length; i++){
			imageButtons.add( (ImageButton) findViewById(imageButtonsIds[i]));
			imageButtons.get(i).setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imageButton1:
			UMainActivity.mEkironjiDevice.playVideo(1);
			break;
		case R.id.imageButton2:
			UMainActivity.mEkironjiDevice.playVideo(2);
			break;
		case R.id.imageButton3:
			UMainActivity.mEkironjiDevice.playVideo(3);
			break;
		}
		
	}
	
	
	

	
}
