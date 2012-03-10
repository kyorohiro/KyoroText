package info.kyorohiro.helloworld.stress.service;

import info.kyorohiro.helloworld.stress.KyoroApplication;
import info.kyorohiro.helloworld.stress.KyoroSetting;
import info.kyorohiro.helloworld.util.KyoroMemoryInfo;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.NotificationManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class KilledProcessStarter implements Runnable {
	private KyoroMemoryInfo mInfos = new KyoroMemoryInfo();

	public void run() {
		int len = KyoroStressService.JavaHeapEater.length;
		
		List<RunningAppProcessInfo> list = getRunningAppList();

		for (int i=0;i<len;i++) {
			task(KyoroStressService.JavaHeapEater[i],KyoroStressService.ServiceProcessName[i],list, i);
			Thread.yield();
			if(Thread.interrupted()){
				break;
			}
		}		
	}

	private List<RunningAppProcessInfo> getRunningAppList() {
		List<RunningAppProcessInfo> list = mInfos.getRunningAppList(KyoroApplication.getKyoroApplication());
		return list;
	}

	private void task(Class clazz, String processName,	List<RunningAppProcessInfo> list, int id) {
		String c = KyoroApplication.getKyoroApplication().getApplicationContext().getPackageName()+":"+processName;
		for(RunningAppProcessInfo i : list) {
			if(i.equals(c)){
				if(!KyoroStressService.START_SERVICE.equals(KyoroSetting.getData(processName))){
					//KyoroStressService.startService(clazz, KyoroApplication.getKyoroApplication(),"end");
					KyoroStressService.stopService(clazz, KyoroApplication.getKyoroApplication());
					Thread.yield();
				}
				return;
			}
			Thread.yield();
		}

		if(KyoroStressService.START_SERVICE.equals(KyoroSetting.getData(processName))){
			KyoroStressService.startService(clazz, KyoroApplication.getKyoroApplication(), "restart");
			Thread.yield();
		}
	}
}
