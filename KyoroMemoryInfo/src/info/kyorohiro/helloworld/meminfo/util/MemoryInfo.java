package info.kyorohiro.helloworld.meminfo.util;


import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public class MemoryInfo {

	public List<RunningAppProcessInfo>  getRunningAppList(Context context) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApp = manager.getRunningAppProcesses();
		return runningApp;
	}

	public android.os.Debug.MemoryInfo getMemInfoData(Context context, int pid) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

		int pids[] = new int[1];
		pids[0] = pid;

		android.os.Debug.MemoryInfo[] memoryInfoArray = manager.getProcessMemoryInfo(pids);
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
		return memoryInfoArray[0];
	}

	static public class MemInfoData {
		
	}
}
