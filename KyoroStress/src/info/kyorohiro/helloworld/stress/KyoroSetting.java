 package info.kyorohiro.helloworld.stress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class KyoroSetting {

	private static Object lock = new Object();
	public static final String TAG_MEMSIZE = "eadup_size_kb";
	public static final String TAG_NUM_OF_BIGEATER = "NumOfBigEater";
	public static final int NUM_OF_BIGEATER_DEFAULT_VALUE = 12;
	public static final int MEMSIZE_DEFAULT_VALUE = 1024 * 10;

	public static void setBigEaterState(String id, String value) {
		setData(KyoroApplication.getKyoroApplication(), id, value, id);
		android.util.Log.v("kiyo","s"+id+""+value);
	}

	public static String getBigEaterState(String id) {
		String value = getData(KyoroApplication.getKyoroApplication(), id, id);
		android.util.Log.v("kiyo","g"+id+","+value);
		return value;
	}

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
		setData(context, property, value, null);
	}

	public static String getData(Context context, String property) {
		return getData(context, property, null);
	}

	public static void setData(Context context, String property, String value, String tag) {
		SharedPreferences pref = null; 
		synchronized (lock) {
			if (tag == null) {
				pref = PreferenceManager.getDefaultSharedPreferences(context);
			} else {
				pref = context.getSharedPreferences(tag, Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
			}
			pref.edit().putString(property, value).commit();
		}
	}

	public static String getData(Context context, String property, String tag) {
		SharedPreferences pref = null; 
		synchronized (lock) {
			if (tag == null) {
				pref = PreferenceManager.getDefaultSharedPreferences(context);
			} else {
				pref = context.getSharedPreferences(tag, Context.MODE_WORLD_READABLE);
			}
			return pref.getString(property, "none");
		}
	}

}
