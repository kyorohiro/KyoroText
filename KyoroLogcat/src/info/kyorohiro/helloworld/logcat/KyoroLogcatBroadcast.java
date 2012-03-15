package info.kyorohiro.helloworld.logcat;

import java.util.Calendar;

import info.kyorohiro.helloworld.logcat.tasks.TaskManagerForSave;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KyoroLogcatBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Context appContext = KyoroApplication.getKyoroApplication().getApplicationContext();
		if(TaskManagerForSave.saveTaskIsForceKilled()) {
			// pf force kill process
			TaskManagerForSave.startSaveTask(appContext);
		}
		if(TaskManagerForSave.saveTaskIsAlive() || TaskManagerForSave.saveTaskIsForceKilled()) {
			 startTimer();
		}
	}

	private static long sLastStarted = -1;
	public static void startTimer(){
		long currentTime = System.currentTimeMillis();
		if(sLastStarted != -1 && (currentTime-sLastStarted) < 180){
			return;
		}
		sLastStarted = currentTime;
		Context appContext = KyoroApplication.getKyoroApplication().getApplicationContext();
		Intent i = new Intent(appContext, KyoroLogcatBroadcast.class);
		PendingIntent sender = PendingIntent.getBroadcast(appContext, 0, i, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 180);

		AlarmManager am = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}

}
