package info.kyorohiro.helloworld.android.base;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

public abstract class ForegroundService extends TestService {

	public ForegroundService(int IdOfStartForeground) {
		super(IdOfStartForeground);
	}

/*	public void startForground(int resId, String title, String message, PendingIntent contentIntent) {
		
}*/
	public void startForgroundAtOnGoing(int resId, String title, String message, PendingIntent contentIntent) {
		// forground
		Notification n = new Notification(resId, title, System.currentTimeMillis());
		n.setLatestEventInfo(this, title, message, contentIntent);
		n.flags = Notification.FLAG_ONGOING_EVENT;
		startForegroundCompat(n);
	}
}
