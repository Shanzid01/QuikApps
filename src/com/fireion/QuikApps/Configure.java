package com.fireion.QuikApps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class Configure extends Activity {

	float ico_size;
	int txt_size;
	boolean txtVisible;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure);
		
		SharedPreferences spf = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		ico_size=spf.getFloat("ico_size", 64);
		txt_size=spf.getInt("txt_size", 13);
		txtVisible=spf.getBoolean("txtVisible", true);
		SeekBar seek_ico_size=(SeekBar)findViewById(R.id.icon_size);
		SeekBar seek_txt_size=(SeekBar)findViewById(R.id.txt_size);
		final CheckBox chk_txtVisible=(CheckBox)findViewById(R.id.chk_appName);
		seek_ico_size.setProgress((int) ico_size);
		seek_txt_size.setProgress(txt_size);
		chk_txtVisible.setChecked(txtVisible);
		final LinearLayout lnParent=(LinearLayout)findViewById(R.id.config_Icoparent);
		final ImageView ico=(ImageView)findViewById(R.id.prev_ico);
		ico.getLayoutParams().height=(int) ((ico_size/100)*lnParent.getLayoutParams().width);
		ico.getLayoutParams().width=(int) ((ico_size/100)*lnParent.getLayoutParams().width);
		final TextView txt=(TextView)findViewById(R.id.prev_txt);
		txt.setTextSize(txt_size);
		if(!txtVisible){
			txt.setVisibility(View.GONE);
		}else{
			txt.setVisibility(View.VISIBLE);
		}
		
		chk_txtVisible.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				txtVisible=chk_txtVisible.isChecked();
				if(!txtVisible){
					txt.setVisibility(View.GONE);
				}else{
					txt.setVisibility(View.VISIBLE);
				}
			}
			
		});
		
		seek_ico_size.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int parentWidth;
			LayoutParams lp;
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
        		if(progresValue>=20){
        			ico_size=progresValue;
        			lp.height=(int) ((ico_size/100)*parentWidth);
        			lp.width=(int) ((ico_size/100)*parentWidth);
        			ico.setLayoutParams(lp);
        		}
        	}
        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        		if(seekBar.getProgress()>=20){
        			lp.height=(int) ((ico_size/100)*parentWidth);
        			lp.width=(int) ((ico_size/100)*parentWidth);
        			ico.setLayoutParams(lp);
        		}
        	}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				parentWidth=lnParent.getLayoutParams().width;
				lp=ico.getLayoutParams();
			}
        });
		
		seek_txt_size.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
        		if(progresValue>=10){
        			txt_size=progresValue;
        			txt.setTextSize(txt_size);
        		}
        	}
        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        		if(seekBar.getProgress()>=10){
        			txt_size=seekBar.getProgress();
        			txt.setTextSize(txt_size);
        		}
        	}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}
        });
	}
	public void save(View x){
		SharedPreferences spf = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		editor.putFloat("ico_size", ico_size);
		editor.putInt("txt_size", txt_size);
		editor.putBoolean("txtVisible", txtVisible);
		editor.commit();
		Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
		if(Panel.active){
			Panel.performChanges();
		}
		super.onBackPressed();
	}
	public void cancel(View c){
		Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.configure, menu);
		return false;
	}

}
