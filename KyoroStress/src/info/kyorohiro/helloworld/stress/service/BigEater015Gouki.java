package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater015Gouki extends KyoroStressService {

	public BigEater015Gouki() {
		super(115);
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_15;
	}

}
