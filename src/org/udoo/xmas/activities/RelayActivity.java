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

import org.udoo.xmas.R;
import org.udoo.xmas.UMainActivity;

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
				UMainActivity.mEkironjiDevice.switchRelay(i);
		}
		
	}
	
	
	

	
}
