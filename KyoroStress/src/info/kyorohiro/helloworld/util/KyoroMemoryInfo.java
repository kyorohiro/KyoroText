package info.kyorohiro.helloworld.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class KyoroMemoryInfo {
	public List<RunningAppProcessInfo>  getRunningAppList(Context context) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApp = manager.getRunningAppProcesses();
		return runningApp;
	}

	public ActivityManager.MemoryInfo getMemoryInfo(Context context) {
	    ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
	    activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo;
	}

	public String memInfo(Context context, int pid) {
	    ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
	    activityManager.getMemoryInfo(memoryInfo);
	    String extra = "";
	    if(Build.VERSION.SDK_INT >= 5) {
	    	int[] pids = new int[]{pid};
	    	android.os.Debug.MemoryInfo[] infos= getMemInfoData(context, pids);
	    	if(infos != null && infos.length >0){
	    		android.os.Debug.MemoryInfo info = infos[0];
	    		extra = ":"+
	    		//",dpd=" + info.dalvikPrivateDirty +
	    		//",dp=" + info.dalvikPss+ 
	    		//",dsd=" + info.dalvikSharedDirty +
	    		//",npd=" + info.nativePrivateDirty +
	    		//",np=" + info.nativePss +
	    		//",nsd=" + info.nativeSharedDirty +
	    		//",opd=" + info.otherPrivateDirty +
	    		//",op=" + info.otherPss +
	    		",TPD=" + info.getTotalPrivateDirty()+
	    		"kb,TPss=" + info.getTotalPss() + 
	    		"kb,TSD=" + info.getTotalSharedDirty()+"kb";
	    	}
	    }
	     return 
	     /*":avail="+(int)(memoryInfo.availMem/1024/1024)+
	     "MB,LBV="+(int)memoryInfo.threshold/1024/1024+
	     "MB,"+memoryInfo.lowMemory + */extra;

	}

	public android.os.Debug.MemoryInfo[] getMemInfoData(Context context, int[] pids) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		android.os.Debug.MemoryInfo[] memoryInfoArray = manager.getProcessMemoryInfo(pids);
		return memoryInfoArray;
	}

	public static void systemOut(android.os.Debug.MemoryInfo[] memoryInfoArray) {
		for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray) {
			Log.v("kyorohiro", "===================================================");
			Log.v("kyorohiro", "getTotalPrivateDirty: " + pidMemoryInfo.getTotalPrivateDirty());
			Log.v("kyorohiro", "getTotalSharedDirty: " + pidMemoryInfo.getTotalSharedDirty());
			Log.v("kyorohiro", "getTotalPss: " + pidMemoryInfo.getTotalPss());
			Log.v("kyorohiro", "---------------------------------------------------");
			Log.v("kyorohiro", "dalvikPrivateDirty: " + pidMemoryInfo.dalvikPrivateDirty);
			Log.v("kyorohiro", "dalvikPss: " + pidMemoryInfo.dalvikPss);
			Log.v("kyorohiro", "dalvikSharedDirty: " + pidMemoryInfo.dalvikSharedDirty);
			Log.v("kyorohiro", "nativePrivateDirty: " + pidMemoryInfo.nativePrivateDirty);
			Log.v("kyorohiro", "nativePss: " + pidMemoryInfo.nativePss);
			Log.v("kyorohiro", "nativeSharedDirty: " + pidMemoryInfo.nativeSharedDirty);
			Log.v("kyorohiro", "otherPrivateDirty: " + pidMemoryInfo.otherPrivateDirty);
			Log.v("kyorohiro", "otherPss: " + pidMemoryInfo.otherPss);
			Log.v("kyorohiro", "otherSharedDirty: " + pidMemoryInfo.otherSharedDirty);
			Log.v("kyorohiro", "===================================================");
		}
	}

	static public class MemInfoData {

	}
}