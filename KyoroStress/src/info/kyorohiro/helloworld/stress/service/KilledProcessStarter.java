package info.kyorohiro.helloworld.stress.service;

import info.kyorohiro.helloworld.stress.KyoroApplication;
import info.kyorohiro.helloworld.stress.KyoroSetting;
import info.kyorohiro.helloworld.util.KyoroMemoryInfo;

import java.util.List;

import android.app.ActivityManager.RunningAppProcessInfo;

public class KilledProcessStarter implements Runnable {
	public void run() {
		int len = KyoroStressService.JavaHeapEater.length;
		for (int i=0;i<len;i++) {
			task(KyoroStressService.JavaHeapEater[i],KyoroStressService.ServiceProcessName[i]);
			Thread.yield();
		}
	}

	private void task(Class clazz, String processName) {
		KyoroMemoryInfo infos = new KyoroMemoryInfo();
		List<RunningAppProcessInfo> list = null;
		list = infos.getRunningAppList(KyoroApplication.getKyoroApplication());
		String c = KyoroApplication.getKyoroApplication().getApplicationContext().getPackageName()+":"+processName;

		for(RunningAppProcessInfo i : list) {
			if(i.equals(c)){
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
