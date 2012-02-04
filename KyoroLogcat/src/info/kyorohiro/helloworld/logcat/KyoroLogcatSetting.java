package info.kyorohiro.helloworld.logcat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KyoroLogcatSetting {

	public static String SAVE_TASK_TAG = "save task";
	public static String SAVE_TASK_IS_STARTED = "started";
	public static String SAVE_TASK_IS_STOPPED= "stopped";

	public static void saveTaskStateIsStart() {
		setData(KyoroApplication.getKyoroApplication(), SAVE_TASK_TAG,SAVE_TASK_IS_STARTED);
	}

	public static void saveTaskStateIsStop() {
		setData(KyoroApplication.getKyoroApplication(), SAVE_TASK_TAG,SAVE_TASK_IS_STOPPED);
	}
	
	public static String getSaveTaskState() {
		return getData(KyoroApplication.getKyoroApplication(), SAVE_TASK_TAG);
	}

	public static void setData(Context context, String property, String value) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		pref.edit().putString(property, value).commit();
	}
	
	public static String getData(Context context, String property) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		return  pref.getString(property, "none");
	}

}
