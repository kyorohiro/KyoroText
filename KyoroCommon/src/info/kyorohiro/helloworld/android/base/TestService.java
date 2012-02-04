package info.kyorohiro.helloworld.android.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

public abstract class TestService extends Service {
	private static final Class<?>[] mSetForegroundSignature = 
		new Class[] {boolean.class};
	private static final Class<?>[] mStartForegroundSignature = 
		new Class[] {int.class, Notification.class};
	private static final Class<?>[] mStopForegroundSignature = 
		new Class[] {boolean.class};

	private NotificationManager mNM;
	private Method mSetForeground;
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mSetForegroundArgs = new Object[1];
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];
	private int mIdOfStartForeground = 1;

	public TestService(int IdOfStartForeground) {
		super();
		mIdOfStartForeground = IdOfStartForeground;
		// TODO Auto-generated constructor stub
	}

	void invokeMethod(Method method, Object[] args) {
		try {
			method.invoke(this, args);
		} catch (InvocationTargetException e) {
			// Should not happen.
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// Should not happen.
                    	e.printStackTrace();
		}
	}

	/**
	 * This is a wrapper around the new startForeground method, using the older
	 * APIs if it is not available.
	 */
	public void startForegroundCompat(Notification notification) {
		int id = mIdOfStartForeground;
		// If we have the new startForeground API, then use it.
		if (mStartForeground != null) {
			mStartForegroundArgs[0] = Integer.valueOf(id);
			mStartForegroundArgs[1] = notification;
			invokeMethod(mStartForeground, mStartForegroundArgs);
			return;
		}

		// Fall back on the old API.
		mSetForegroundArgs[0] = Boolean.TRUE;
		invokeMethod(mSetForeground, mSetForegroundArgs);
		mNM.notify(id, notification);
	}

	/**
	 * This is a wrapper around the new stopForeground method, using the older
	 * APIs if it is not available.
	 */
	public void stopForegroundCompat() {
		int id = mIdOfStartForeground;
		// If we have the new stopForeground API, then use it.
		if (mStopForeground != null) {
			mStopForegroundArgs[0] = Boolean.TRUE;
			invokeMethod(mStopForeground, mStopForegroundArgs);
			return;
		}

		// Fall back on the old API.  Note to cancel BEFORE changing the
		// foreground state, since we could be killed at that point.
		mNM.cancel(id);
		mSetForegroundArgs[0] = Boolean.FALSE;
		invokeMethod(mSetForeground, mSetForegroundArgs);
	}

	
	public static void cancelForgroundNotificationAfterProcessKill(Context context) {
		try {
			NotificationManager mNM = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
			mNM.cancel(1);
		} catch(Exception e) {
		}
	}

	@Override
	public void onCreate() {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		if(Build.VERSION.SDK_INT > 7){
			try {
				mStartForeground = getClass().getMethod("startForeground",
						mStartForegroundSignature);
				mStopForeground = getClass().getMethod("stopForeground",
						mStopForegroundSignature);
				return;
			} catch (NoSuchMethodException e) {
				mStartForeground = mStopForeground = null;
			}
		}
		try {
			mSetForeground = getClass().getMethod("setForeground",
					mSetForegroundSignature);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(
					"OS doesn't have Service.startForeground OR Service.setForeground!");
		}
	}

	@Override
	public void onDestroy() {
		// Make sure our notification is gone.
		stopForegroundCompat();
	}
}
