package com.example.radioequestria;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Widget_main extends AppWidgetProvider{

	RemoteViews remoteViews;
	ComponentName thisWidget;

	Timer timer;
	TimerTask infoTask;
	final MediaPlayer mp = null;
	static boolean playing = false;
	public static String PLAY_ACTION = "com.example.radioequestria.PLAY";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.widget_main);
        if (action.equals(PLAY_ACTION)){
        	if(playing == true){
        		Player.stop();
        		playing = false;
        		Log.i("mp", "stop");
        		rm.setImageViewResource(R.id.imageButton1, R.drawable.play);
        		AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget_main.class), rm);
        	}
        	else{
    			playing = true;
    			
    			rm.setViewVisibility(R.id.imageButton1, View.INVISIBLE);
    			rm.setViewVisibility(R.id.progressBar1, View.VISIBLE);
    			AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget_main.class), rm);
    			try {
    				rm.setImageViewResource(R.id.imageButton1, R.drawable.pause);
					Player.play(context);
				} catch (Exception e){
					Log.w("mp", "EXCEPTION!!" + e.toString());
					
					rm.setImageViewResource(R.id.imageButton1, R.drawable.play);
					playing = false;
					rm.setViewVisibility(R.id.progressBar1, View.INVISIBLE);
	    			rm.setViewVisibility(R.id.imageButton1, View.VISIBLE);
					AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget_main.class), rm);
					
				} 
    			
    			Log.i("mp", "play");
    		}
        	
        }
		
		super.onReceive(context, intent);
	}

	

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		if((Player.mp == null) || Player.mp.isPlaying()){
			Player.stop();
		}
		if(timer != null)
			timer.cancel();
		if(infoTask != null)
			infoTask.cancel();
		android.os.Process.killProcess(android.os.Process.myPid());

		super.onDisabled(context);

	}



	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		if((Player.mp == null) || Player.mp.isPlaying()){
			Player.stop();
		}
		if(timer != null)
			timer.cancel();
		if(infoTask != null)
			infoTask.cancel();
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		
	}



	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);
		thisWidget = new ComponentName(context, Widget_main.class);
		
		Intent play = new Intent(context, Widget_main.class);
		play.setAction(PLAY_ACTION);
		PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, play, 0);
		remoteViews.setOnClickPendingIntent(R.id.imageButton1, playPendingIntent);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		
		final Context kontekst = context;
		final Handler handler = new Handler();
		infoTask = new TimerTask(){

			@Override
			public void run() {
				handler.post(new Runnable(){

					@Override
					public void run() {
						new Informations(kontekst).execute();						
					}					
				});				
			}		
		};
		timer = new Timer();
		timer.schedule(infoTask, 0, 10000);
		
	}



	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "Aplikacja RadioEquestria jest darmowa, jednak ³¹cz¹c siê za pomoc¹ sieci, mo¿e pobieraæ op³aty wed³ug taryfy operatora. Aby unikn¹æ wydatków, zalecamy ³¹czyæ siê poprzez WiFi.", Toast.LENGTH_LONG).show();

		super.onEnabled(context);
	}




	
	

	

}
