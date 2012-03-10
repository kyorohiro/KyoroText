package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class BigEater011Gouki extends KyoroStressService {

	public BigEater011Gouki() {
		super(111);
	}

	@Override
	public String getProperty() {
		return KyoroStressService.ID_11;
	}

}
