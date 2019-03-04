package com.fireion.QuikApps;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Panel extends Service {
	View mView;
	ArrayList<String> pckg=new ArrayList<String>();
	ArrayList<String> appName=new ArrayList<String>();
	ArrayList<Drawable> icons=new ArrayList<Drawable>();
	Context myC=this;
	public static boolean active=false;
	WindowManager mWindowManager;
	WindowManager wm;
	WindowManager.LayoutParams mParams;
	WindowManager.LayoutParams bp;
	View mew;
	int widtl;
	int widtl2;
	public static boolean sFin=true;
	public static LinearLayout mn;
	float asap;
	boolean moved=false;
	int edit_y;
	int ask;
	
	float ico_size;
	int txt_size;
	boolean txtVisible;
	double mnW;
	static Context con;
	
	ArrayList<ImageView> icons_display=new ArrayList<ImageView>();
	ArrayList<TextView> txt_display=new ArrayList<TextView>();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if(!active){
    		active=true;
    		con=this;
    		getVars();
    		go();
    		displayNotification();
    	}
    	else{
    		Toast.makeText(this, "The process is already running", Toast.LENGTH_SHORT).show();
    	}
		return super.onStartCommand(intent, flags, startId);
    }
    
    public void getVars(){
    	SharedPreferences spf = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		ico_size=spf.getFloat("ico_size", 64);
		txt_size=spf.getInt("txt_size", 13);
		txtVisible=spf.getBoolean("txtVisible", true);
    }
    public static void performChanges(){
    	((Panel) con).getVars();
    	((Panel) con).goChanges();
    }
    public void goChanges(){
    	for(int i=0;i<icons_display.size();i++){
    		ImageView img=icons_display.get(i);
    		LayoutParams lna=img.getLayoutParams();
    		double a=((ico_size/100)*mnW);
    		lna.width=(int) a;
    		lna.height=(int) a;
    		img.setLayoutParams(lna);
    	}
    	for(TextView a:txt_display){
    		a.setTextSize(txt_size);
    		if(!txtVisible){
    			a.getLayoutParams().height=0;
    			a.setVisibility(View.INVISIBLE);
    		}else{
    			a.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
    			a.setVisibility(View.VISIBLE);
    		}
    	}
    }
    protected void displayNotification() {
		Notification notification;
		NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);	
		mBuilder.setContentTitle("QuikApps");
	    mBuilder.setContentText("QuikApps is currently active.");
//	    mBuilder.setSmallIcon(R.drawable.ic_launcher);
	    Intent notificationIntent = new Intent(this, MainActivity.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    mBuilder.setContentIntent(pendingIntent);
	    notification=mBuilder.build();
	    startForeground(Notification.FLAG_ONGOING_EVENT, notification);
	}
	@SuppressWarnings("deprecation")
	public void go(){
        LayoutInflater lia;
        lia = LayoutInflater.from(this);
        mView = lia.inflate(R.layout.lay, null);

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, 150, 0, 0,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        mParams.gravity = Gravity.RIGHT | Gravity.TOP;
        mParams.setTitle("Window Pane");
        mParams.height=WindowManager.LayoutParams.MATCH_PARENT;
        mParams.width=WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mView, mParams);
        
        final ImageView close=(ImageView)mView.findViewById(R.id.close_v);
        final ImageView open=(ImageView)mView.findViewById(R.id.open_v);
        final Button edit=(Button)mView.findViewById(R.id.edit_deg);
        close.setBackgroundDrawable(Data.btn_bg2);
        open.setBackgroundDrawable(Data.btn_bg1);
        edit.setBackgroundColor(Data.edit_btn_color);
        mn=(LinearLayout)mView.findViewById(R.id.mn);
        mn.setBackgroundDrawable(Data.bg);
        mn.getBackground().setAlpha(Data.opacity);

        mn.postDelayed(new Runnable(){
        	@Override
        	public void run(){
        		widtl=mn.getWidth();
        	}
        }, 10);
        edit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				edit.setEnabled(false);
				open.setEnabled(false);
				close.setEnabled(false);
				edit(mn.getWidth());
			}
        });
        edit.postDelayed(new Runnable(){
        	@Override
        	public void run(){
        		edit_y=(int)edit.getTop()-40;
        		asap=close.getTop();
        	}
        }, 10);
        close.setOnTouchListener(new OnTouchListener(){
        	float x1, x2;
			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				 switch (touchevent.getAction())
                 {
                   case MotionEvent.ACTION_DOWN:{
                       x1 = touchevent.getX();
                       break;
                   }
                   case MotionEvent.ACTION_UP:{
                   x2 = touchevent.getX();
                   if (x1 < x2){
                	   close.setVisibility(View.GONE);
                  		open.setVisibility(View.VISIBLE);
                  		mn.setVisibility(View.GONE);
                  		mParams.y=(int)asap;
                  		if(!moved){
                  			ViewGroup.MarginLayoutParams ln=(MarginLayoutParams) close.getLayoutParams();
                      		mParams.height=close.getHeight();
                      		mParams.y=ln.topMargin;
                      		ln.topMargin=0;
                  			open.setLayoutParams(ln);
                  		}else{
                  			mParams.height=close.getHeight();
                  		}
                  		mWindowManager.updateViewLayout(mView, mParams);
                   }
                   break;
                  }
                 }
				return false;
			}
        });
        
        open.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				open.setOnTouchListener(new OnTouchListener(){
					@Override
					public boolean onTouch(View v, MotionEvent touchevent) {
						switch (touchevent.getAction()){
			                case MotionEvent.ACTION_MOVE:{
				                	float b=touchevent.getRawY()-v.getHeight();
				                	float c=10;
				                	if(b>c && b<edit_y){
						                 asap=touchevent.getRawY()-v.getHeight();
						                 ViewGroup.MarginLayoutParams laa=(ViewGroup.MarginLayoutParams)open.getLayoutParams();
								         mParams.y=(int)asap;
								         laa.topMargin=0;
								         open.setLayoutParams(laa);
								         mWindowManager.updateViewLayout(mView, mParams);
								         moved=true;
				                	}
				                	break;
			                 }
			                case MotionEvent.ACTION_UP:{
			                	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v.getWidth(),  v.getHeight());
						         params.setMargins(0, (int)(mParams.y), 0, 0);
						         close.setLayoutParams(params);
						         v.setOnTouchListener(new OnTouchListener() {
						     	    @Override
						     	    public boolean onTouch(View v, MotionEvent rawEvent) {
						     	        return false;
						     	    }
						     	});
						         break;
			                }
		                }
						return false;
					}
		        });
				return true;
			}
        	
        });
        close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				close.setVisibility(View.GONE);
           		open.setVisibility(View.VISIBLE);
           		mn.setVisibility(View.GONE);
           		mParams.y=(int)asap;
           		if(!moved){
           			ViewGroup.MarginLayoutParams ln=(MarginLayoutParams) close.getLayoutParams();
               		mParams.height=close.getHeight();
               		ask=ln.topMargin;
               		mParams.y=ln.topMargin;
               		ln.topMargin=0;
           			open.setLayoutParams(ln);
           		}else{
           			mParams.height=close.getHeight();
           		}
           		mWindowManager.updateViewLayout(mView, mParams);
			}
        });
        open.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				close.setVisibility(View.VISIBLE);
          		open.setVisibility(View.GONE);
          		mn.setVisibility(View.VISIBLE);
          		mParams.y=0;
          		if(!moved){
          			ViewGroup.MarginLayoutParams ln=(MarginLayoutParams) open.getLayoutParams();
          			ln.topMargin=ask;
          			close.setLayoutParams(ln);
          		}
          		mParams.height=WindowManager.LayoutParams.MATCH_PARENT;
          		mWindowManager.updateViewLayout(mView, mParams);
			}
        });
                
        Toast.makeText(getBaseContext(), "Compiling Items...", Toast.LENGTH_SHORT).show();
        setUp s=new setUp();
        s.execute();
    } 
    private class setUp extends AsyncTask<Void, Float, String> {
    	TextView prgLbl=(TextView)mView.findViewById(R.id.prgLbl);
    	ProgressBar prg=(ProgressBar)mView.findViewById(R.id.pg);
    	float x;
    	float y=0;
	    @Override
	    protected String doInBackground(Void...sock) {
	    	sFin=false;
	    	prg.setVisibility(View.VISIBLE);
	    	prgLbl.setVisibility(View.VISIBLE);
	    	try{
		    	final PackageManager pm = getPackageManager();
				List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
				x=packages.size();
				for (ApplicationInfo packageInfo : packages) {
					publishProgress(new Float[]{y});
					Intent mz = getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
					if(mz!=null){
						pckg.add(packageInfo.packageName);
						icons.add(pm.getApplicationIcon(packageInfo.packageName));
						appName.add(pm.getApplicationLabel(packageInfo).toString());
					}
					y++;
				}
				return "";
			}catch(Exception o){
				Toast.makeText(getBaseContext(), "Error in compiling list: "+o, Toast.LENGTH_LONG).show();
				return "";
			}
	    }
	    
	    @Override
	    protected void onProgressUpdate(Float... n){
	    	int a=(int) ((n[0]/x)*100);
   			prgLbl.setText(a+"%");
	    }
	    @Override
		protected void onPostExecute(String s) {
	    	prgLbl.setVisibility(View.GONE);
	    	prg.setVisibility(View.GONE);
	    	sFin=true;
	    	addBtns a=new addBtns();
	    	a.execute();
		}
    }
    private class addBtns extends AsyncTask<Void, Object, String> {
    	LinearLayout ln;
    	
    	ViewGroup.LayoutParams lp;
    	ViewGroup.LayoutParams lp2;
	    @Override
	    protected String doInBackground(Void...vooid) {    	
	    	try{
		    	ln=(LinearLayout)mView.findViewById(R.id.Ln);
		    	
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				
				
				params2.setMargins(3, 0, 3, 7);
				
				lp=params;
				lp2=params2;
				
				LinearLayout mn=(LinearLayout)mView.findViewById(R.id.mn);
				mnW=mn.getWidth();
				double x=((ico_size/100)*mnW);
				lp.width=(int) x;
				lp.height=(int) x;
				
				for(int i=0; i<icons.size(); i++){
					ImageView a=new ImageView(myC);
					a.setId(i);
					a.setImageDrawable(icons.get(i));
					a.setOnClickListener(get(a));
					icons_display.add(a);
					
					TextView ab=new TextView(myC);
					ab.setId(i);
					ab.setTextSize(txt_size);
					ab.setTextColor(getResources().getColor(R.color.black));
					ab.setGravity(Gravity.CENTER);
					if(!txtVisible){
						params2.height=0;
						ab.setVisibility(View.INVISIBLE);
					}else{
						ab.setVisibility(View.VISIBLE);
					}
					ab.setOnClickListener(get(ab));
					
					if(appName.get(i)!=""){
						ab.setText(appName.get(i));
					}
					else{
						ab.setText("No name");
					}
					txt_display.add(ab);
					
					Object[] obj={(ImageView) a, (TextView) ab}; 
					publishProgress(obj);
				}
				return "";
	    	}catch(Exception e){
	    		return "Error on: "+e;
	    	}
	    }
	   
	    @Override
	    protected void onProgressUpdate(Object... n){
	    	try{
		    	ln.addView((View) n[0], lp);
		    	ln.addView((View) n[1], lp2);
	    	}catch(Exception e){
	    		err("Error when adding to layout: "+e);
	    	}
	    }
	    View.OnClickListener get(final View button)  {
	        return new View.OnClickListener() {
	            public void onClick(View v) {
	            	String x=pckg.get(button.getId());
	                launchApp(x);
	            }
	        };
	    }
	    @Override
		protected void onPostExecute(String a) {
	    	err(a);
		}
    }
    public void err(String e){
    	if(!e.equals("")){
    		Toast.makeText(getBaseContext(), e, Toast.LENGTH_LONG).show();
    	}
    }
    View.OnClickListener get(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
            	String x=pckg.get(button.getId());
                launchApp(x);
            }
        };
    }
    protected void launchApp(String packageName) {
    	try{ 
			Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
			startActivity(mIntent);
    	}catch(Exception e){
    		Toast.makeText(getBaseContext(), "Sorry, application could not be launched..\n"+e, Toast.LENGTH_SHORT).show();
    	}
	}
    
    int wit;
    public void edit(int width){
        LayoutInflater lin;
        lin = LayoutInflater.from(this);
        mew = lin.inflate(R.layout.edit_design, null);

        bp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, 150, 0, 0,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        
        DisplayMetrics metrics = myC.getResources().getDisplayMetrics();
        final int wid = metrics.widthPixels;
        
        final LinearLayout mnl=(LinearLayout)mView.findViewById(R.id.mn);
   		int a=mnl.getWidth();
        wit=wid-a+4;
        bp.gravity = Gravity.LEFT;
        bp.setTitle("Overlay");
        bp.height=WindowManager.LayoutParams.MATCH_PARENT;
        bp.width=wit;
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        wm.addView(mew, bp);
        
        Button done=(Button)mew.findViewById(R.id.doe);
        final Button green=(Button)mew.findViewById(R.id.green);
        LayoutParams ln=green.getLayoutParams();
        ln.height=width;
        ln.width=width;
        
        final Button red=(Button)mew.findViewById(R.id.red);
        final Button pink=(Button)mew.findViewById(R.id.pink);
        final Button yellow=(Button)mew.findViewById(R.id.yellow);
        final Button blue=(Button)mew.findViewById(R.id.blue);
        final Button black=(Button)mew.findViewById(R.id.black);
        
        green.setLayoutParams(ln);
        red.setLayoutParams(ln);
        pink.setLayoutParams(ln);
        yellow.setLayoutParams(ln);
        blue.setLayoutParams(ln);
        black.setLayoutParams(ln);
        
        green.setId(1);
        red.setId(2);
        pink.setId(3);
        yellow.setId(4);
        blue.setId(5);
        black.setId(6);
        
        green.setOnClickListener(a(green));
        red.setOnClickListener(a(red));
        pink.setOnClickListener(a(pink));
        yellow.setOnClickListener(a(yellow));
        blue.setOnClickListener(a(blue));
        black.setOnClickListener(a(black));
        
        done.setOnClickListener(new OnClickListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				bp.height=WindowManager.LayoutParams.WRAP_CONTENT;
				bp.width=WindowManager.LayoutParams.WRAP_CONTENT;
				wm.removeView(mew);
				
				//Finalizing
				Button edit=(Button)mView.findViewById(R.id.edit_deg);
				edit.setEnabled(true);
				mView.findViewById(R.id.open_v).setEnabled(true);
				mView.findViewById(R.id.close_v).setEnabled(true);
				edit.setBackgroundColor(Data.edit_btn_color);
				mn.getBackground().setAlpha(Data.opacity);
				mn.setBackgroundDrawable(Data.bg);
				ImageView open=(ImageView)mView.findViewById(R.id.open_v);
				ImageView close=(ImageView)mView.findViewById(R.id.close_v);
				open.setBackgroundDrawable(Data.btn_bg1);
				close.setBackgroundDrawable(Data.btn_bg2);
				
			}
        });
        
        SeekBar opacity=(SeekBar)mew.findViewById(R.id.opacity);
        opacity.setProgress(Data.opacity);
        opacity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	int progress = 255;
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
        		green.getBackground().setAlpha(progresValue);
        		red.getBackground().setAlpha(progresValue);
        		pink.getBackground().setAlpha(progresValue);
        		yellow.getBackground().setAlpha(progresValue);
        		blue.getBackground().setAlpha(progresValue);
        		black.getBackground().setAlpha(progresValue);
        		progress=progresValue;
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
	        
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        		mn.getBackground().setAlpha(progress);
        		Data.opacity=progress;
        	}
        });
    }

    View.OnClickListener a(final Button button)  {
        return new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
            	String id=String.valueOf(button.getId());
            	ImageView open=(ImageView)mView.findViewById(R.id.open_v);
            	ImageView close=(ImageView)mView.findViewById(R.id.close_v);
            	Button edit=(Button)mView.findViewById(R.id.edit_deg);
            	if(id.equals("1")){
            		Drawable a=getResources().getDrawable(R.drawable.lay_green);
            		mn.setBackgroundDrawable(a);
            		mn.getBackground().setAlpha(Data.opacity);
            		edit.setBackgroundColor(0x00DD77);
            		Drawable bg1=getResources().getDrawable(R.drawable.pnl_green);
            		open.setBackgroundDrawable(bg1);
            		Drawable bg2=getResources().getDrawable(R.drawable.pnl2_green);
            		close.setBackgroundDrawable(bg2);
            		
            		//Putting to Data.class
            		Data.btn_bg1=bg1;
            		Data.btn_bg2=bg2;
            		Data.bg=a;
            		Data.edit_btn_color=0x00DD77;
            	}
            	else if(id.equals("2")){
            		Drawable a=getResources().getDrawable(R.drawable.lay_red);
            		mn.setBackgroundDrawable(a);
            		mn.getBackground().setAlpha(Data.opacity);
            		edit.setBackgroundColor(0xFF4422);
            		Drawable bg1=getResources().getDrawable(R.drawable.pnl_red);
            		open.setBackgroundDrawable(bg1);
            		Drawable bg2=getResources().getDrawable(R.drawable.pnl2_red);
            		close.setBackgroundDrawable(bg2);
            		Data.btn_bg1=bg1;
            		Data.btn_bg2=bg2;
            		Data.bg=a;
            		Data.edit_btn_color=0xFF4422;
            	}
            	else if(id.equals("3")){
            		Drawable a=getResources().getDrawable(R.drawable.lay_pink);
            		mn.setBackgroundDrawable(a);
            		mn.getBackground().setAlpha(Data.opacity);
            		edit.setBackgroundColor(0xFF66CC);
            		Drawable bg1=getResources().getDrawable(R.drawable.pnl_pink);
            		open.setBackgroundDrawable(bg1);
            		Drawable bg2=getResources().getDrawable(R.drawable.pnl2_pink);
            		close.setBackgroundDrawable(bg2);
            		Data.btn_bg1=bg1;
            		Data.btn_bg2=bg2;
            		Data.bg=a;
            		Data.edit_btn_color=0xFF66CC;
            	}
            	else if(id.equals("4")){
            		Drawable a=getResources().getDrawable(R.drawable.lay_yellow);
            		mn.setBackgroundDrawable(a);
            		mn.getBackground().setAlpha(Data.opacity);
            		edit.setBackgroundColor(0xFFCC66);
            		Drawable bg1=getResources().getDrawable(R.drawable.pnl_yellow);
            		open.setBackgroundDrawable(bg1);
            		Drawable bg2=getResources().getDrawable(R.drawable.pnl2_yellow);
            		close.setBackgroundDrawable(bg2);
            		Data.btn_bg1=bg1;
            		Data.btn_bg2=bg2;
            		Data.bg=a;
            		Data.edit_btn_color=0xFFCC66;
            	}
            	else if(id.equals("5")){
            		Drawable a=getResources().getDrawable(R.drawable.back);
            		mn.setBackgroundDrawable(a);
            		mn.getBackground().setAlpha(Data.opacity);
            		edit.setBackgroundColor(0x66eeff);
            		Drawable bg1=getResources().getDrawable(R.drawable.pnl);
            		open.setBackgroundDrawable(bg1);
            		Drawable bg2=getResources().getDrawable(R.drawable.pnl2_blue);
            		close.setBackgroundDrawable(bg2);
            		Data.btn_bg1=bg1;
            		Data.btn_bg2=bg2;
            		Data.bg=a;
            		Data.edit_btn_color=0x66eeff;
            	}
            	else if(id.equals("6")){
            		Drawable a=getResources().getDrawable(R.drawable.lay_black);
            		mn.setBackgroundDrawable(a);
            		mn.getBackground().setAlpha(Data.opacity);
            		edit.setBackgroundColor(R.drawable.lay_red);
            		edit.setBackgroundColor(0x0000);
            		Drawable bg1=getResources().getDrawable(R.drawable.pnl_black);
            		open.setBackgroundDrawable(bg1);
            		Drawable bg2=getResources().getDrawable(R.drawable.pnl2_black);
            		close.setBackgroundDrawable(bg2);
            		Data.btn_bg1=bg1;
            		Data.btn_bg2=bg2;
            		Data.bg=a;
            		Data.edit_btn_color=0x0000;
            	}
            }
        };
    }
    @Override
    public void onDestroy() {
        setUp s=new setUp();
        addBtns a=new addBtns();
        s.cancel(true);
        a.cancel(true);
        mParams.height=0;
        mParams.width=0;
        //bp.width=0;
        //bp.height=0;
        mWindowManager.updateViewLayout(mView, mParams);
        //wm.updateViewLayout(mew, bp);
        mWindowManager.removeView(mView);
        //wm.removeView(mew);
        stopForeground(true);
        active=false;
    }
    
}
