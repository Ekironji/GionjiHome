package com.gionji.gionjihome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gionji.gionjihome.net.UDPClientBroadcastAsyncTask;
import com.gionji.gionjihome.net.UDPClientBroadcastAsyncTask.IPAddressServerListener;
import com.gionji.gionjihome.net.UDPSendCommandThread;

public class GHMainActivity extends Activity implements OnClickListener{

	ImageButton[] mImageButtons = new ImageButton[3];
	TextView[] mTextViews       = new TextView[3];
	
	private boolean[] relays = {false, false, false};
	private String[]  relaysNames = {"Luce","Luce","-"};
		
	private final static String MY_PREFERENCES   = "MyPref";
	private final static String IP_ADDRESS_KEY   = "IpAddress"; 
	private final static String RELAY_NAMES_KEY  = "RelayNames"; 
	private final static String NETWORK_SSID_KEY = "SSIDName"; 
	
	private final static String RELAY_1_LABEL_KEY = "relay1";
	private final static String RELAY_2_LABEL_KEY = "relay2";
	private final static String RELAY_3_LABEL_KEY = "relay3";
	
	static SharedPreferences prefs;
	
	String ghSsid    = "";
	String ipAddress = "192.168.1.37";
	String ghId      = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ghmain);
		
		mImageButtons[0] = (ImageButton) findViewById(R.id.imageButton1);
		mImageButtons[1] = (ImageButton) findViewById(R.id.imageButton2);
		mImageButtons[2] = (ImageButton) findViewById(R.id.imageButton3);
		
		mTextViews[0] = (TextView) findViewById(R.id.textView1);
		mTextViews[1] = (TextView) findViewById(R.id.textView2);
		mTextViews[2] = (TextView) findViewById(R.id.textView3);
		
		mImageButtons[0].setOnClickListener(this);
		

		prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
		prefs.getString(RELAY_1_LABEL_KEY, "");	
		prefs.getString(RELAY_2_LABEL_KEY, "");	
		prefs.getString(RELAY_3_LABEL_KEY, "");	
		
		
		for(int i=0; i<mImageButtons.length; i++){
			mImageButtons[i].setOnClickListener(this);
			if(relays[i])
				mImageButtons[i].setAlpha(1F);
			else
				mImageButtons[i].setAlpha(0.4F);
		}
		
//		
//		prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
//		ipAddress = prefs.getString(IP_ADDRESS_KEY, null);		
//		
//		if (ipAddress != null) {
//			
//		}
		
		
		// sono connesso alla wifi
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()){
			//wifi is enabled
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mWifi.isConnected()) {
				searchGionjiHome();	
			}
			else{
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		}
		else{
			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		}
		
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
			searchGionjiHome();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imageButton1:		
			switchRelay(0);
			break;
		case R.id.imageButton2:	
			switchRelay(1);
			break;
		case R.id.imageButton3:	
			switchRelay(2);
			break;
		}
	}
	
	private void switchRelay(int i){
		relays[i] = !relays[i];
		if(relays[i])
			mImageButtons[i].setAlpha(1.0f);		
		else
			mImageButtons[i].setAlpha(0.4f);	
		
		sendRelayCommand();
	}
	
	
	private void sendRelayCommand(){
		new UDPSendCommandThread(ipAddress, getRelayPacket()).start();
	}
	
	
	private byte getRelayPacket(){
		byte msg = 0x40;
		for(int i=0; i<relays.length; i++){
			if(relays[i])
				msg |= (0x01 << i);
		}
		return msg;
	}
	
	
	private void searchGionjiHome(){
		UDPClientBroadcastAsyncTask task = new UDPClientBroadcastAsyncTask(this);
		task.setIPAddressServerListener(new IPAddressServerListener() {
			@Override
			public void IPAddressServerFounded(String response) {
				Toast.makeText(getApplicationContext(),
						"GionjiHome " + getId(response)  + " found! ip: " + getIpAddress(response), Toast.LENGTH_SHORT)
						.show();
			
				ipAddress = getIpAddress(response);
				ghId = getId(response);
//				ip.setText(address);
//				SharedPreferences.Editor editor = prefs.edit();
//				editor.putString(IP_ADDRESS_KEY, address);
//				editor.commit();
			}

			@Override
			public void IPAddressServerFailed() {
				Toast.makeText(getApplicationContext(),
						"Abajour not found :-(", Toast.LENGTH_SHORT).show();
			}
		});
		task.setProgressDialogMessage("Wait until abajour is found...");
		task.execute();
		
	}
	
	
	static public String getIpAddress(String msg){
		return msg.split("@")[1];
	}
	
	static public String getId(String msg){
		return msg.split("#")[1].split("@")[0];
	}
	
}
