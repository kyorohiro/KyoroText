package info.kyorohiro.helloworld.stress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KyoroSetting {


	public static void setData(String property, String value) {
		setData(KyoroApplication.getKyoroApplication(), property, value);
	}
	
	public static String getSaveTaskState(String property) {
		return getData(KyoroApplication.getKyoroApplication(), property);
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
