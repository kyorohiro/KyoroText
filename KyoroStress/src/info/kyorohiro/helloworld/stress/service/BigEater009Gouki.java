package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater009Gouki extends KyoroStressService {

	public BigEater009Gouki() {
		super(109);
	}

	public static Intent startService(Context context, String message) {
		Intent startIntent = new Intent(context, BigEater009Gouki.class);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    return startIntent;
	}

	public static int getColor() {
		return Color.WHITE;
	}

	public static String getNickName() {
		return "No09 JavaHeapEater";
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_09;
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}

}
