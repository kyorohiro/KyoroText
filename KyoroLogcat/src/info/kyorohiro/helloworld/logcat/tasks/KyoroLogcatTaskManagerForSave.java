package info.kyorohiro.helloworld.logcat.tasks;

import info.kyorohiro.helloworld.logcat.tasks.SaveCurrentLogTask;
import info.kyorohiro.helloworld.logcat.widget.KyoroSaveWidget;
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
		} catch (Throwable t) {
			sSaveTask = null;
		}
	}

	public static void stopSaveTask(Context context) {
		if(!saveTaskIsAlive()){
			return;
		}
		sSaveTask.terminate();
	}
}
