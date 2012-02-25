package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater001Gouki extends KyoroStressService {

	public BigEater001Gouki() {
		super(1);
	}

	@Override
	public String getProperty() {
		return "First";
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}

}
