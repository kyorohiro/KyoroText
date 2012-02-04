package info.kyorohiro.helloworld.logcat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import info.kyorohiro.helloworld.android.base.TestService;
import info.kyorohiro.helloworld.logcat.tasks.TaskManagerForSave;

/**
 * change a permanent process to use this class.
 */
public class KyoroLogcatService extends TestService {

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

	public static Intent startLogcatService(Context context) {
		Intent startIntent = new Intent(context, KyoroLogcatService.class);
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
			KyoroApplication.showMessageAndNotification("spmode process is killed");
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Notification n = new Notification(R.drawable.ic_launcher, "kyorologcat", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, KyoroLogcatActivity.class), 0);
		n.setLatestEventInfo(this, "kyoro logcat", "run background to save log", contentIntent);
		n.flags = Notification.FLAG_ONGOING_EVENT;
		startForegroundCompat(n);
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
