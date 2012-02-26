package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater004Gouki extends KyoroStressService {

	public BigEater004Gouki() {
		super(104);
	}

	public static Intent startService(Context context, String message) {
		Intent startIntent = new Intent(context, BigEater004Gouki.class);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    return startIntent;
	}

	public static String getNickName() {
		return "No04 JavaHeapEater";
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_04;
	}

	public static int getColor() {
		return Color.GRAY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

}
