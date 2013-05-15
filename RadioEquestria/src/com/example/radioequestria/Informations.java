package com.example.radioequestria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;

public class Informations extends AsyncTask<Void, Void, Void>{
	
	String status;
	String prezenter;
	String audycja;
	String song;
	Context context;
	


	String serwer = "http://4stream.pl:18328/";
	
	public Informations(Context context) {
		this.context = context;
	}
	

	void download() throws IOException{

		URL u = new URL(serwer);
		URLConnection uc = u.openConnection();
		HttpURLConnection connection = (HttpURLConnection) uc;
		connection.setDoInput(true);
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer stringBuffer = new StringBuffer();
		Log.i("BUFFER", "reading");
		String string;
		   while((string = reader.readLine()) != null){
		    stringBuffer.append(string + "\n");
		   }
		Log.i("BUFFER", "ok");
		inputStream.close();
		String page = stringBuffer.toString();
		
		page = Html.fromHtml(page).toString();
		string = page.substring(page.indexOf("Server Status:"));
		Log.i("serwer", string);
		
		if(string.contains("down")){
			status = "offline";
			Log.i("oo", "getted" + status);
			prezenter = "Nie nadajemy";
			audycja = "Brak audycji";
			song = "Radio Equestria.FM, zapraszamy na forum MLPPolska.pl! Pozdrawiamy wszystkich s³uchaczy!";
		}else{
			status = "online";
			prezenter = page.substring(page.indexOf("Genre: ") +7, page.indexOf("Stream URL:"));
			audycja = page.substring(page.indexOf("Stream Title: ") +14, page.indexOf("Content Type:"));
			song = page.substring(page.indexOf("Current Song: ") +14, page.indexOf("Written by Stephen"));

		}
		Log.i("status", status);
		Log.i("prezenter", prezenter);
		Log.i("audycja", audycja);
		Log.i("song", song);
		CurrentInfo.set(status, prezenter, audycja, song);
		CurrentInfo.connected = true;
		
	}

	@Override
	protected void onPostExecute(Void result) {
		RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.widget_main);
		
		rm.setTextViewText(R.id.textView2, CurrentInfo.prezenter);
		rm.setTextViewText(R.id.textView3, CurrentInfo.audycja);
		rm.setTextViewText(R.id.textView1, CurrentInfo.song);
		if(CurrentInfo.isChanged()){
			Log.i("WIDGET", "zmieniam...");
			AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget_main.class), rm);
		}
		super.onPostExecute(result);
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			download();
		} catch (IOException e) {
			status = "offline";
			Log.w("html", "IO");
			prezenter = "Brak po³¹czenia";
			audycja = "";
			song = "SprawdŸ ustawienia po³¹czenia internetowego i spróbuj ponownie";
			CurrentInfo.set(status, prezenter, audycja, song);
			e.printStackTrace();
			CurrentInfo.connected = false;
		}
		
		return null;
	}

}
