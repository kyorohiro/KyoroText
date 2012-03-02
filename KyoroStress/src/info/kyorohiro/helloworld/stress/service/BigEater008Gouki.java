package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater008Gouki extends KyoroStressService {

	public BigEater008Gouki() {
		super(108);
	}

	public static Intent startService(Context context, String message) {
		Intent startIntent = new Intent(context, BigEater008Gouki.class);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    return startIntent;
	}

	public static Intent stopService(Context context) {
		Intent startIntent = new Intent(context, BigEater008Gouki.class);
	    context.stopService(startIntent);
	    return startIntent;
	}

	public static int getColor() {
		return Color.WHITE;
	}

	public static String getNickName() {
		return "No08 JavaHeapEater";
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_08;
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}

}
