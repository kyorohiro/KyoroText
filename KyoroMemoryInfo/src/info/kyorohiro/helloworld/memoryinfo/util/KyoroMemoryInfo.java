package info.kyorohiro.helloworld.memoryinfo.util;

import info.kyorohiro.helloworld.memoryinfo.KyoroApplication;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class KyoroMemoryInfo {
	public static String NAME_TAG_dalvikPrivateDirty = "/dalvikPrivateDirty";
	public static String NAME_TAG_dalvikPss = "/dalvikPss";
	public static String NAME_TAG_dalvikSharedDirty = "/dalvikSharedDirty";
	public static String NAME_TAG_nativePrivateDirty = "/nativePrivateDirty";
	public static String NAME_TAG_nativePss = "/nativePss";
	public static String NAME_TAG_nativeSharedDirty = "/nativeSharedDirty";
	public static String NAME_TAG_otherPrivateDirty = "/otherPrivateDirty";
	public static String NAME_TAG_otherPss = "/otherPss";
	public static String NAME_TAG_totalPrivateDirty = "/totalPrivateDirty";
	public static String NAME_TAG_totalPss = "/totalPss";
	public static String NAME_TAG_totalSharedDirty = "/totalSharedDirty";
	public static String NAME_TAG_number = "/number";
	public static String NAME_TAG_time = "/time";
	public static String NAME_TAG_type = "/type";
	public static String NAME_TAG_processName = "/processName";
	public static String NAME_TAG_availMem = "/availMem";
	public static String NAME_TAG_threshold = "/threshold";

	public KyoroMemoryInfo() {
	}

	private List<RunningAppProcessInfo>  getRunningAppList(Context context) {
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
	
	public String update() {
		Context context = KyoroApplication.getKyoroApplication();
		List<RunningAppProcessInfo> appInfoList = getRunningAppList(context);
		RunningAppProcessInfo[] info = new RunningAppProcessInfo[appInfoList.size()];
		for(int i=0;i<info.length;i++){
			info[i] = appInfoList.get(i);
		}
		return memInfo(context, info);
	}

	int mNumber=0;
	public int getNumber() {
		return mNumber++;
	}

	public String memInfo(Context context, RunningAppProcessInfo[] in) {
		int number = getNumber();
		long time = System.currentTimeMillis();
	    String extra = "";
	    int[] pids = new int[in.length];
	    for(int i=0;i<pids.length;i++){
	    	pids[i] = in[i].pid;
	    }

	    android.os.Debug.MemoryInfo[] infos= getMemInfoData(context, pids);	
	    if(infos == null) {
	    	return ""; 
	    }

	    // create index total memory
	    ActivityManager.MemoryInfo info0 = getMemoryInfo(context);
	    extra += "<<"+
	    	NAME_TAG_number          +" ("+ number                       +")\n"+
		    NAME_TAG_time            +" ("+ time                         +")\n"+
		    NAME_TAG_type            +" ("+ "activitymanager.memoryinfo" +")\n"+
		    NAME_TAG_availMem        +" ("+ info0.availMem               +")\n"+
		    NAME_TAG_threshold       +" ("+ info0.threshold              +")\n"+
	    	">>";

	    // create index for debuginfo
	    for(int i=0;i<infos.length;i++) {
	    	android.os.Debug.MemoryInfo info = infos[i];
	    	RunningAppProcessInfo in2 = in[i];
	    	extra +=
	       "<<"+
            NAME_TAG_number             +" ("+ number                  +")\n"+
	        NAME_TAG_time               +" ("+ time                    +")\n"+
		    NAME_TAG_type               +" ("+ "debug.memoryinfo"      +")\n"+
	    	NAME_TAG_processName        +" ("+ in2.processName         +")\n"+
	    	NAME_TAG_dalvikPrivateDirty +" ("+ info.dalvikPrivateDirty +")\n"+
	    	NAME_TAG_dalvikPss          +" ("+ info.dalvikPss          +")\n"+
	    	NAME_TAG_dalvikSharedDirty  +" ("+ info.dalvikSharedDirty  +")\n"+
	    	NAME_TAG_nativePrivateDirty +" ("+ info.nativePrivateDirty +")\n"+
	    	NAME_TAG_nativePss          +" ("+ info.nativePss          +")\n"+
	    	NAME_TAG_nativeSharedDirty  +" ("+ info.nativeSharedDirty  +")\n"+
	    	NAME_TAG_otherPrivateDirty  +" ("+ info.otherPrivateDirty  +")\n"+
	    	NAME_TAG_otherPss           +" ("+ info.otherPss           +")\n"+
	    	NAME_TAG_totalPrivateDirty  +" ("+ info.getTotalPrivateDirty() +")\n"+
	    	NAME_TAG_totalPss           +" ("+ info.getTotalPss()          +")\n"+
	    	NAME_TAG_totalSharedDirty   +" ("+ info.getTotalSharedDirty()  +")\n"+
	    	">>";
	    }
	    return extra;
	}

	public android.os.Debug.MemoryInfo[] getMemInfoData(Context context, int[] pids) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		android.os.Debug.MemoryInfo[] memoryInfoArray = manager.getProcessMemoryInfo(pids);
		return memoryInfoArray;
	}
}