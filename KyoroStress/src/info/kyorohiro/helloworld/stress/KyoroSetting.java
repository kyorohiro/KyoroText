package info.kyorohiro.helloworld.stress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KyoroSetting {

	public static final String TAG_MEMSIZE = "eadup_size_kb";
	public static final int MEMSIZE_DEFAULT_VALUE = 1024*10;
	
	public static int getEatupHeapSize() {
		int ret = MEMSIZE_DEFAULT_VALUE;

		String memsizeAsString = getData(TAG_MEMSIZE);
		if(memsizeAsString != null && !memsizeAsString.equals("none")){
			int t = Integer.parseInt(memsizeAsString);
			if (t < 100) {
				ret = MEMSIZE_DEFAULT_VALUE;			
			} else {
				ret = t;
			}
			
		}
		return ret;
	}

	public static void setEatupHeadSize(String size) {
		setData(TAG_MEMSIZE, size);
	}

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
