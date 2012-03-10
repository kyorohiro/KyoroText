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

	@Override
	public String getProperty() {
		return KyoroStressService.ID_02;
	}
}
