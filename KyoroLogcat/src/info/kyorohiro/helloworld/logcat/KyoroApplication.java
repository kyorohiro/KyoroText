package info.kyorohiro.helloworld.logcat;

import info.kyorohiro.helloworld.logcat.widget.KyoroSaveWidget;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class KyoroApplication extends Application {

	private Handler mHandler = null;
	private static KyoroApplication sInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		mHandler = new Handler();
		KyoroLogcatService.cancelForgroundNotificationAfterProcessKill(this);
	}

	public static void shortcutToStartKyoroLogcatService() throws NullPointerException {
		KyoroLogcatService.startLogcatService(sInstance);
		KyoroSaveWidget.setStopImage(sInstance);
	}

	public static void shortcutToStopKyoroLogcatService() throws NullPointerException {
		KyoroLogcatService.stopLogcatService(null, null);
		KyoroSaveWidget.setSaveImage(sInstance);
	}

	public static void showMessageAndNotification(String message) {
		try {
			showMessage(message);

			Notification n = new Notification(R.drawable.ic_notification, "kyorologcat", System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(sInstance, 0, new Intent(sInstance, KyoroLogcatActivity.class), 0);
			n.setLatestEventInfo(sInstance, "kyoro logcat", ""+message, contentIntent);
			n.flags = Notification.FLAG_ONLY_ALERT_ONCE;
			NotificationManager mNM = (NotificationManager)sInstance.getSystemService(NOTIFICATION_SERVICE);
			mNM.notify(R.drawable.ic_notification, n);
		} catch(Throwable e){
			e.printStackTrace();
		}		
	}
	public static void showMessage(String message) {
		try {
			sInstance.mHandler.post(new ToastMessage(sInstance, message));
		} catch(Throwable e){
			e.printStackTrace();
		}
	}

	private static class ToastMessage implements Runnable {
		private Context mContext = null;
		private String mMessage = null;
		public ToastMessage(Context c, String m) {
			mContext = c;
			mMessage = m;
		}
		public void run(){
			try {
				Toast.makeText(mContext.getApplicationContext(), mMessage, Toast.LENGTH_SHORT).show();
				mContext = null;
				mMessage = null;
			} catch(Throwable e){
				e.printStackTrace();
			} 
		}
	}
}
