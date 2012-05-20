package info.kyorohiro.helloworld.memoryinfo;


import info.kyorohiro.helloworld.android.base.ForegroundService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class KyoroMemoryInfoService extends ForegroundService {
		
	public static Intent startService(Context context, String message) {
		Intent startIntent = new Intent(context, KyoroMemoryInfoService.class);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    KyoroMemoryInfoBroadcast.startTimer();
	    return startIntent;
	}

	public static Intent stopService(Context context) {
		Intent startIntent = new Intent(context, KyoroMemoryInfoService.class);
	    context.stopService(startIntent);
	    return startIntent;
	}

	public KyoroMemoryInfoService() {
		super(131);
	}		

	@Override
	public void onCreate() {
		super.onCreate();
//		stopForegroundCompat();
	}


	@Override
	public void onStartHandle(Intent intent) {
		super.onStartHandle(intent);
		startForground(":nn");
	}

	public void startForground(String messagePlus) {
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, KyoroMemoryInfoActivity.class), 0);
			int resId = R.drawable.ic_launcher;
			String title = "kyoro memory info";
			String message = messagePlus;
			startForgroundAtOnGoing(resId, title, message, contentIntent);
	}

	@Override
	public IBinder onBind(Intent intent) {return null;}


}
