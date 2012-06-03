package info.kyorohiro.helloworld.memoryinfo;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KyoroMemoryInfoBroadcast extends BroadcastReceiver {

	private static long sLastStarted = -1;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Context appContext = KyoroApplication.getKyoroApplication().getApplicationContext();
		if(KyoroSetting.isForground()) {
			KyoroMemoryInfoService.startService(appContext, "nn");
		}
	}

	public static void startTimer(){
		long currentTime = System.currentTimeMillis();
		if(sLastStarted != -1 && (currentTime-sLastStarted) < 180){
			return;
		}
		sLastStarted = currentTime;
		Context appContext = KyoroApplication.getKyoroApplication().getApplicationContext();
		Intent i = new Intent(appContext, KyoroMemoryInfoBroadcast.class);
		PendingIntent sender = PendingIntent.getBroadcast(appContext, 0, i, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 180);

		AlarmManager am = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}

}