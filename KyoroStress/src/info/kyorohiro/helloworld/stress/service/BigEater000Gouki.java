package info.kyorohiro.helloworld.stress.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class BigEater000Gouki extends KyoroStressService {

	public BigEater000Gouki() {
		super(100);
	}
	@Override
	public String getProperty() {
		return KyoroStressService.ID_00;
	}

}
