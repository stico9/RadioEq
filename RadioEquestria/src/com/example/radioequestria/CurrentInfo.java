package com.example.radioequestria;

import android.util.Log;

public class CurrentInfo {
	public static String status = "";
	public static String prezenter = "";
	public static String audycja = "";
	public static String song = "";
	public static boolean changed = true;
	public static boolean connected = false;
	
	public static synchronized void set(String s, String p, String a, String so){
		if((!s.equals(status)) || (!p.equals(prezenter)) || (!a.equals(audycja)) || (!so.equals(song))){
			Log.i("Info", "ZMIANA" + s + ":" + status);
			status = s;
			prezenter = p;
			audycja = a;
			song = so;
			changed = true;
			
		}else{
			changed = false;
			Log.i("Info", "BEZ ZMIAN!");
		}
	}
	public static boolean isChanged(){
		return changed;
	}
}
