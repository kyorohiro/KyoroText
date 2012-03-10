package info.kyorohiro.helloworld.stress.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import info.kyorohiro.helloworld.android.base.ForegroundService;
import info.kyorohiro.helloworld.stress.KyoroSetting;
import info.kyorohiro.helloworld.stress.KyoroStressActivity;
import info.kyorohiro.helloworld.stress.R;
import info.kyorohiro.helloworld.stress.task.EatUpJavaHeapTask;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;

public abstract class KyoroStressService extends ForegroundService {

	private LinkedList<byte[]> mBuffer = new LinkedList<byte[]>();
	private Thread mTask = null;
	private long startTime = System.currentTimeMillis();

	public static final String START_SERVICE = "start";
	public static final String STOP_SERVICE = "stop";

	public static String ID_00 = "no00";
	public static String ID_01 = "no01";
	public static String ID_02 = "no02";
	public static String ID_03 = "no03";
	public static String ID_04 = "no04";
	public static String ID_05 = "no05";
	public static String ID_06 = "no06";
	public static String ID_07 = "no07";
	public static String ID_08 = "no08";
	public static String ID_09 = "no09";
	public static String ID_10 = "no10";
	public static String ID_11 = "no11";
	public static String ID_12 = "no12";
	public static String ID_13 = "no13";
	public static String ID_14 = "no14";
	public static String ID_15 = "no15";
	public static String ID_16 = "no16";
	
	private String mCurrentDisplayMessage = "";

	public final static String ServiceProcessName[] = new String[]{
		ID_00,ID_01, ID_02, ID_03,
		ID_04,ID_05, ID_06, ID_07,
		ID_08,ID_09, ID_10, ID_11,
		ID_12,ID_13, ID_14, ID_15,
		ID_16			
	};

	public final static Class JavaHeapEater[] = new Class[] {
		BigEater000Gouki.class,
		BigEater001Gouki.class, 
		BigEater002Gouki.class,
		BigEater003Gouki.class,
		BigEater004Gouki.class,
		BigEater005Gouki.class,
		BigEater006Gouki.class,
		BigEater007Gouki.class,
		BigEater008Gouki.class,
		BigEater009Gouki.class,
		BigEater010Gouki.class,
		BigEater011Gouki.class, 
		BigEater012Gouki.class,
		BigEater013Gouki.class,
		BigEater014Gouki.class,
		BigEater015Gouki.class,
		BigEater016Gouki.class,
	};

		
	public static Intent startService(Class clazz, Context context, String message) {
		Intent startIntent = new Intent(context, clazz);
	    if(message != null){
	    	startIntent.putExtra("message", message);
	    }
	    context.startService(startIntent);
	    return startIntent;
	}

	public static Intent stopService(Class clazz, Context context) {
		Intent startIntent = new Intent(context, clazz);
	    context.stopService(startIntent);
	    return startIntent;
	}

	public abstract String getProperty();

	public KyoroStressService(int IdOfStartForeground) {
		super(IdOfStartForeground);
	}	
	
	public String getLabel() {
		return "KyoroStress";
	}

	public String getMessage() {
		return "eatup java heap";
	}
	

	@Override
	public void onCreate() {
		super.onCreate();
		stopForegroundCompat();
		mBuffer = new LinkedList<byte[]>(); 
		if (START_SERVICE.equals(""+KyoroSetting.getData(getProperty()))) {
			startForground("restart");
		} 
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String message = "";
		if(intent !=null && null != intent.getExtras()){
		    message = intent.getExtras().getString("message");
		}

		if(message != null && message.equals("end")){
			// when still cant not showed notificartion
			long endTime = System.currentTimeMillis();
			long waitTime = (endTime-startTime);
			Handler h = new Handler();
			if(waitTime<2000){
				waitTime = 2000;
			}
			h.postDelayed(new DelayAndStop(),waitTime);
			return;
		}

		if (!START_SERVICE.equals(""+KyoroSetting.getData(getProperty()))) {
			Handler h = new Handler();
			h.postDelayed(new DelayAndStop(),100);
			return;
		}

		if(startTask()){
			startForground(message);
		}
	}

	private class DelayAndStop implements Runnable{
		@Override
		public void run() {
			stopForegroundCompat();
			stopSelf();	
		}
	}


	public void startForground(String messagePlus) {
			mCurrentDisplayMessage = messagePlus;
			startTime = System.currentTimeMillis();
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, KyoroStressActivity.class), 0);
			int resId = R.drawable.ic_launcher;
			String title = getLabel()+":"+ getProperty();
			String message = getMessage()+":"+messagePlus;
			startForgroundAtOnGoing(resId, title, message, contentIntent);
	}

	@Override
	public void onDestroy() {
		android.util.Log.v("kiyohiro","onDestry()"+getClass().getName());
		super.onDestroy();
		if(mTask != null){
			mTask.interrupt();
			mTask = null;
		}
		endTask();
	}

	@Override
	public IBinder onBind(Intent intent) {return null;}

	
	//
	// Task
	//
	private Thread mTh = null;

	public boolean startTask() {
		if(mTh == null /*|| !mTh.isAlive()*/) {
			mTh = new MyStarter(new EatUpJavaHeapTask(mBuffer));
			mTh.start();
			return true;
		}
		else {
			return false;
		}
	}

	public void endTask() {
		if(mTh !=null && mTh.isAlive()) {
			mTh.interrupt();
			mTh = null;
		}
	}

	public class MyStarter extends Thread {
		private KilledProcessStarter mTask = new KilledProcessStarter();
		public MyStarter(Runnable runnable) {
			super(runnable);
		}

		@Override
		public void run() {
			try {
				super.run();
				startForground("eatuped");
				mTask.run();
			} finally {

			}
		}
	}
}
