package info.kyorohiro.helloworld.logcat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import info.kyorohiro.helloworld.android.base.ForegroundService;
import info.kyorohiro.helloworld.android.base.TestService;
import info.kyorohiro.helloworld.logcat.tasks.TaskManagerForSave;

/**
 * change a permanent process to use this class.
 */
public class KyoroLogcatService extends ForegroundService  {

	private static KyoroLogcatService instance = null;
	public static KyoroLogcatService getCurrentInstance() {
		return instance;
	}

	public static boolean isAliveLogcatService() {
		if(instance == null){
			return false;
		}
		else {
			return true;
		}
	}

	public static Intent startLogcatService(Context context, String message) {
		Intent startIntent = new Intent(context, KyoroLogcatService.class);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    return startIntent;
	}

	public static void stopLogcatService(Context context, Intent intent) {
		if(instance != null){
			instance.stopForegroundCompat();
		}

		if(intent==null || context == null){
			if(instance != null){
				instance.stopSelf();
			}
		}
		else {
			context.stopService(intent);
		}
	}

	
	public KyoroLogcatService() {
		super(1);
	}

	@Override
	public void onCreate() {
		// PF call only onCreate() when restarting service 
		// after PF call force stop this service.
		super.onCreate();
		instance = this;
		// todo 
		if(TaskManagerForSave.saveTaskIsForceKilled()) {
			// force kill process
			TaskManagerForSave.startSaveTask(getApplicationContext());
			KyoroApplication.showMessageAndNotification("process is killed");
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String message = "run background to save log";
		if(intent != null && intent.getExtras() != null && intent.getExtras().getString("message") != null){
			message = intent.getExtras().getString("message");
		}
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, KyoroLogcatActivity.class), 0);
		int resId = R.drawable.ic_launcher;
		String title = "kyorologcat";
		startForground(resId, title, message, contentIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	@Override
	public IBinder onBind(Intent arg0) {return null;}

	@Override
	public boolean onUnbind(Intent intent) {return super.onUnbind(intent);}

}
