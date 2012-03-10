package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater001Gouki extends KyoroStressService {

	public BigEater001Gouki() {
		super(101);
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_01;
	}

}
