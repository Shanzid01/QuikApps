package com.fireion.QuikApps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
	Button b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(!Data.acti){
			startService(new Intent(this, Data.class));
		}
		
		b=(Button)findViewById(R.id.sta_sto);
		if(!Panel.active){
			b.setText("Open Panel");
		}
		else{
			b.setText("Close Panel");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}
	public void edit(View c){
		Intent i=new Intent(this, Configure.class);
		startActivity(i);
	}
	public void start(View v){
		if(!Panel.sFin){
			Toast.makeText(getBaseContext(), "Please wait till all the services are closed...", Toast.LENGTH_SHORT).show();
		}
		else{
			if(!Panel.active){
				b.setText("Close Panel");
				startService(new Intent(this, Panel.class));
			}
			else{
				b.setText("Open Panel");
				stopService(new Intent(this, Panel.class));
			}
		}
	}
}
