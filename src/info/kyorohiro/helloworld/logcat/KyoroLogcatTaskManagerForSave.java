package info.kyorohiro.helloworld.logcat;

import android.content.Context;

public class KyoroLogcatTaskManagerForSave {
	private static  SaveCurrentLogTask sSaveTask = null;

	public static boolean saveTaskIsAlive() {
		if (sSaveTask != null && sSaveTask.isAlive()) {
			return true;
		} else {
			return false;
		}
	}

	public static void startSaveTask(Context context) {
		try {
			if(saveTaskIsAlive()){
				return;
			}
			sSaveTask = new SaveCurrentLogTask("-v time");
			sSaveTask.start();
//			KyoroLogcatService.startLogcatService(context.getApplicationContext());
		} catch (Throwable t) {
			sSaveTask = null;
		}
	}

	public static void stopSaveTask() {
		if(!saveTaskIsAlive()){
			return;
		}
		sSaveTask.terminate();
//		KyoroLogcatService.stopLogcatService(null, null);
	}
}
