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
import android.os.IBinder;

public abstract class KyoroStressService extends ForegroundService {

	private LinkedList<byte[]> mBuffer = new LinkedList<byte[]>();
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
		if (START_SERVICE.equals(""+KyoroSetting.getData(getProperty()))) {
			startInternal("restart");
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String message = "";
		if(intent !=null && null != intent.getExtras()){
		    message = intent.getExtras().getString("message");
		}
		if(message != null && message.equals("restart")){
			// Restart 不要
			if (!START_SERVICE.equals(""+KyoroSetting.getData(getProperty()))) {
				stopSelf();
				return;
			}
		}

		if(message != null && message.equals("end")){
			stopSelf();
			return;
		}


		//KyoroSetting.setData(getProperty(), START_SERVICE);
		startInternal(message);
	}

	private void startInternal(String message) {
		if(startTask()){
			startForground(message);
		}		
	}

	public void startForground(String messagePlus) {
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, KyoroStressActivity.class), 0);
		int resId = R.drawable.ic_launcher;
		String title = getLabel()+":"+ getProperty();
		String message = getMessage()+":"+messagePlus;
		startForground(resId, title, message, contentIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		endTask();
	}

	@Override
	public IBinder onBind(Intent intent) {return null;}

	
	//
	// Task
	//
	private Thread mTh = null;

	public boolean startTask() {
		if(mTh == null || !mTh.isAlive()) {
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
		public MyStarter(Runnable runnable) {
			super(runnable);
		}

		@Override
		public void run() {
			try {
				super.run();
				startForground(":size="+mBuffer.size()*EatUpJavaHeapTask.mAtomSize);
			} finally {

			}
		}
	}
    public static void startService(Class clazz, Context context, String message) {
    	Method method;
    	try {
    		method = clazz.getMethod("startService", new Class[]{ Context.class, String.class });
    	} catch (SecurityException e) {
    		throw new RuntimeException(e);
    	} catch (NoSuchMethodException e) {
    		throw new RuntimeException(e);
    	}

    	Object ret; //戻り値
    	try {
    		ret = method.invoke(clazz, new Object[]{ context, message });
    		//  = object.メソッド((int)1); と同じ
    	} catch (IllegalArgumentException e) {
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		e.printStackTrace();
    	} catch (InvocationTargetException e) {
    		e.printStackTrace();
    	}
    }


}
