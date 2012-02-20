package info.kyorohiro.helloworld.logcat.tasks;

import info.kyorohiro.helloworld.logcat.KyoroApplication;
import info.kyorohiro.helloworld.logcat.KyoroLogcatSetting;
import info.kyorohiro.helloworld.logcat.tasks.SaveCurrentLogTask;
import info.kyorohiro.helloworld.logcat.widget.KyoroSaveWidget;
import android.content.Context;
import android.os.Environment;

public class TaskManagerForSave {
	private static  SaveCurrentLogTask sSaveTask = null;

	public static boolean saveTaskIsAlive() {
		if (sSaveTask != null && sSaveTask.isAlive()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean saveTaskIsForceKilled() {
		boolean isAliveFromPreference = false;
		boolean isAliveFromHeap = saveTaskIsAlive();
		if(KyoroLogcatSetting.getSaveTaskState().equals(
				KyoroLogcatSetting.SAVE_TASK_IS_STARTED)){
			isAliveFromPreference = true;
		}
		else {
			isAliveFromPreference = false;
		}
		if(isAliveFromHeap== false&&isAliveFromPreference==true){
			return true;
		}
		else {
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
			// todo 常時起動の部分の状態繊維の記述が汚いので、修正する。
			KyoroApplication.shortcutToStopKyoroLogcatService();
			KyoroLogcatSetting.saveTaskStateIsStop();
			return;
		}
		sSaveTask.terminate();
	}
}
