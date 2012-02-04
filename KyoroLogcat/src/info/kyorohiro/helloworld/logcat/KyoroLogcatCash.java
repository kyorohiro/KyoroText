package info.kyorohiro.helloworld.logcat;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KyoroLogcatCash {

	private static HashMap<String, String> mCash = new HashMap<String, String>(); 
	public static String START_TASK_TAG = "start task";
	public static String START_TASK_VALUE_SHOW_FOLDER = "show folder";

	public static void startTaskToShowFolder() {
		setData(START_TASK_TAG, START_TASK_VALUE_SHOW_FOLDER);
	}	

	public static void startTaskToNone() {
		setData(START_TASK_TAG, "none");
	}	

	public static boolean startTaskIsShowFolder() {
		String v = getData(START_TASK_TAG);
		if (START_TASK_VALUE_SHOW_FOLDER.equals(v)){
			return true;
		} else {
			return false;
		}
	}

	private static void setData(String tag, String value) {
		mCash.put(tag, value);
	}

	private static String getData(String tag) {
		return ""+mCash.get(tag);
	}
}
