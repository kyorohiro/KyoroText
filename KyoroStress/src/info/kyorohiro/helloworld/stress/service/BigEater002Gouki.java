package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater002Gouki extends KyoroStressService {

	public BigEater002Gouki() {
		super(102);
	}

	public static Intent startService(Context context, String message) {
		Intent startIntent = new Intent(context, BigEater002Gouki.class);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    return startIntent;
	}

	public static String getNickName() {
		return "No02 JavaHeapEater";
	}

	public static int getColor() {
		return Color.RED;
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_02;
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}

}
