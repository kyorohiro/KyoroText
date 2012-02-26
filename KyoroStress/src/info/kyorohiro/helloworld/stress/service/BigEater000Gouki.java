package info.kyorohiro.helloworld.stress.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class BigEater000Gouki extends KyoroStressService {

	public BigEater000Gouki() {
		super(100);
	}

	public static Intent startService(Context context, String message) {
		Intent startIntent = new Intent(context, BigEater000Gouki.class);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    return startIntent;
	}

	public static String getNickName() {
		return "No00 JavaHeapEater";
	}

	public static int getColor() {
		return Color.YELLOW;
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_00;
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}

}
