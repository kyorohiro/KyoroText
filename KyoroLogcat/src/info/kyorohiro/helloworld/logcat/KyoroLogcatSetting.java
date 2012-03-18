package info.kyorohiro.helloworld.logcat;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class KyoroLogcatSetting {


	public static String SAVE_TASK_TAG = "save task";
	public static String SAVE_TASK_IS_STARTED = "started";
	public static String SAVE_TASK_IS_STOPPED= "stopped";

	// following tag is mistak. but released so. 
	// could not change.
	public static String OPTION_TAG = "-v -time";
	public static String OPTION_DEFAULT = "-v time";

	public static final String SAVEDDIR = "KyoroLogcat";

	public static File getHomeDirInSDCard() {
		File fsys= Environment.getExternalStorageDirectory();
		File savedDir = new File(fsys, SAVEDDIR);
		if(!savedDir.exists()) {
			savedDir.mkdirs();
		}
		if(savedDir.isFile()){
			return fsys;
		}
		return savedDir;
	}

	public static String getLogcatOption() {
		String option = getData(KyoroApplication.getKyoroApplication(), OPTION_TAG);
		if(option == null || option.equals("none")){
			option = OPTION_DEFAULT;
		}
		return option;
	}

	public static void setDefaultLogcatOption() {
		setData(KyoroApplication.getKyoroApplication(),OPTION_TAG, "none");		
	}

	public static void setLogcatOption(String option) {
		setData(KyoroApplication.getKyoroApplication(),OPTION_TAG, option);		
	}

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
