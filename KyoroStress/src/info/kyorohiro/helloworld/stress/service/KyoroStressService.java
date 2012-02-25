package info.kyorohiro.helloworld.stress.service;

import java.util.LinkedList;

import info.kyorohiro.helloworld.android.base.ForegroundService;
import info.kyorohiro.helloworld.android.base.TestService;
import info.kyorohiro.helloworld.stress.KyoroSetting;
import info.kyorohiro.helloworld.stress.KyoroStressActivity;
import info.kyorohiro.helloworld.stress.R;
import info.kyorohiro.helloworld.stress.R.drawable;
import info.kyorohiro.helloworld.stress.task.EatUpJavaHeapTask;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;

public abstract class KyoroStressService extends ForegroundService {

	private LinkedList<byte[]> mBuffer = new LinkedList<byte[]>();
	public static final String START_SERVICE = "start";
	public static final String STOP_SERVICE = "stop";

	public KyoroStressService(int IdOfStartForeground) {
		super(IdOfStartForeground);
	}

	public abstract String getProperty();
	
	public String getLabel() {
		return "KyoroStress";
	}

	public String getMessage() {
		return "eatup java heap";
	}
	
	public String getAction() {
		return "info.kyorohiro.helloworld.stress.ACTION";
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBuffer = new LinkedList<byte[]>(); 
		if (START_SERVICE.equals(""+KyoroSetting.getSaveTaskState(getProperty()))) {
			startTask();			
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		KyoroSetting.setData(getProperty(), START_SERVICE);
		KyoroBroadCast.registReceiver(this, getAction());
		startTask();
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, KyoroStressActivity.class), 0);
		int resId = R.drawable.ic_launcher;
		String title = getLabel();
		String message = getMessage();
		startForground(resId, title, message, contentIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		KyoroSetting.setData(getProperty(), STOP_SERVICE);
		endTask();
	}

	@Override
	public IBinder onBind(Intent intent) {return null;}

	
	//
	// Task
	//
	private Thread mTh = null;

	public void startTask() {
		if(mTh == null || !mTh.isAlive()) {
			mTh = new Thread(new EatUpJavaHeapTask(mBuffer));
			mTh.start();
		}
	}

	public void endTask() {
		if(mTh !=null || mTh.isAlive()) {
			mTh.interrupt();
			mTh = null;
		}
	}

	//
	// Message
	//
	
}
