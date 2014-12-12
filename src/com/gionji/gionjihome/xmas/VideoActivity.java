package com.gionji.gionjihome.xmas;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.gionji.gionjihome.GHMainActivity;
import com.gionji.gionjihome.R;

public class VideoActivity extends Activity implements OnClickListener{
	
	
	private int[] buttonsIds = {R.id.button1, R.id.button2, R.id.button3};
	ArrayList<Button> buttons = new ArrayList<Button>();
	
	WebView mWebView = null;
	private String address = "192.168.0.110";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		mWebView = (WebView) findViewById(R.id.webView1);
		
		for(int i=0; i<buttonsIds.length; i++){
			buttons.add( (Button) findViewById(buttonsIds[i]));
			buttons.get(i).setOnClickListener(this);
		}
		
		mWebView.loadUrl("http://" + address  + "/index.php");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button1:
			GHMainActivity.mEkironjiDevice.playVideo(1);
			break;
		case R.id.button2:
			break;
		case R.id.button3:
			break;
		}
		
	}
	
	
	

	
}
