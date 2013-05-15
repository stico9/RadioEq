package com.example.radioequestria;

import java.io.IOException;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class Player {
	public static MediaPlayer mp;
	
	public static void play(final Context context) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException, Exception{
		if(mp == null){
			mp = new MediaPlayer();
		}
		if(mp.isPlaying()){
			return;
		}
		if(CurrentInfo.connected == false){
			throw new IOException();
		}
		final RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.widget_main);
		AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget_main.class), rm);
		
    	mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setDataSource("http://4stream.pl:18328/");
		mp.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra){
                mp.release();
                Log.e("mp", "ERROR");
                rm.setImageViewResource(R.id.imageButton1, R.drawable.play);
                rm.setViewVisibility(R.id.progressBar1, View.INVISIBLE);
    			rm.setViewVisibility(R.id.imageButton1, View.VISIBLE);
    			AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget_main.class), rm);
    			Widget_main.playing = false;
    			mp = null;
                return false;
            }
        });
		
		mp.setOnPreparedListener(new OnPreparedListener(){

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				rm.setImageViewResource(R.id.imageButton1, R.drawable.pause);
				rm.setViewVisibility(R.id.progressBar1, View.INVISIBLE);
    			rm.setViewVisibility(R.id.imageButton1, View.VISIBLE);
    			AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget_main.class), rm);
			}
			
		});
		mp.prepareAsync();
		
		

	}
	
	public static void stop(){
		if(mp == null){
			return;
		}
		if(mp.isPlaying()){
			mp.stop();
			mp.release();
			mp = null;
		}
	}
}
