package info.kyorohiro.helloworld.stress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KyoroSetting {

	public static void setData(String property, String value) {
		//android.util.Log.v("kiyohiro","set:"+property+":"+value);
		setData(KyoroApplication.getKyoroApplication(), property, value);
	}
	
	public static String getData(String property) {
		String v = getData(KyoroApplication.getKyoroApplication(), property);
		//android.util.Log.v("kiyohiro2","set:"+property);
		return v;
	}

	private static Object lock =  new Object();
	public static void setData(Context context, String property, String value) {
		synchronized(lock){
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			pref.edit().putString(property, value).commit();
		}
	}
	
	public static String getData(Context context, String property) {
		synchronized(lock){
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			return  pref.getString(property, "none");
		}
	}

}
