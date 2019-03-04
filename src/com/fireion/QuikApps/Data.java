package com.fireion.QuikApps;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.IBinder;

public class Data extends Service{
	
	public static Drawable bg;
	public static Drawable btn_bg1;
	public static Drawable btn_bg2;
	public static int edit_btn_color;
	public static int opacity;
	
	public static boolean acti=false;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		acti=true;
		bg=getResources().getDrawable(R.drawable.back);
		btn_bg1=getResources().getDrawable(R.drawable.pnl);
		btn_bg2=getResources().getDrawable(R.drawable.pnl2_blue);
		edit_btn_color=0x33ddff;
		opacity=255;
		return Service.START_STICKY;
	}
	

}
