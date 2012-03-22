package info.kyorohiro.helloworld.stress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KyoroSetting {

	private static Object lock = new Object();
	public static final String TAG_MEMSIZE = "eadup_size_kb";
	public static final String TAG_NUM_OF_BIGEATER = "NumOfBigEater";
	public static final int NUM_OF_BIGEATER_DEFAULT_VALUE = 12;
	public static final int MEMSIZE_DEFAULT_VALUE = 1024 * 10;

	public static void setNumOfBigEater(String num) {
		setData(TAG_NUM_OF_BIGEATER, num);
	}

	public static int getNumOfBigEater() {
		int ret = NUM_OF_BIGEATER_DEFAULT_VALUE;
		try {
			String valueAsString = getData(TAG_NUM_OF_BIGEATER);
			if(valueAsString != null && !valueAsString.equals("none")) {
				ret = Integer.parseInt(valueAsString);
				if(ret<3){
					ret = NUM_OF_BIGEATER_DEFAULT_VALUE;
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
		return ret;
	}
	
	public static int getEatupHeapSize() {
		int ret = MEMSIZE_DEFAULT_VALUE;

		try {
			String memsizeAsString = getData(TAG_MEMSIZE);
			if (memsizeAsString != null && !memsizeAsString.equals("none")) {
				int t = Integer.parseInt(memsizeAsString);
				if (t < 100) {
					ret = MEMSIZE_DEFAULT_VALUE;
				} else {
					ret = t;
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}

		return ret;
	}

	public static void setEatupHeadSize(String size) {
		setData(TAG_MEMSIZE, size);
	}

	public static void setData(String property, String value) {
		setData(KyoroApplication.getKyoroApplication(), property, value);
	}

	public static String getData(String property) {
		String v = getData(KyoroApplication.getKyoroApplication(), property);
		return v;
	}

	public static void setData(Context context, String property, String value) {
		synchronized (lock) {
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);
			pref.edit().putString(property, value).commit();
		}
	}

	public static String getData(Context context, String property) {
		synchronized (lock) {
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);
			return pref.getString(property, "none");
		}
	}

}
