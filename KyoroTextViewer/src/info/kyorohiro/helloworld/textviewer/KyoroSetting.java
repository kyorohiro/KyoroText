 package info.kyorohiro.helloworld.textviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class KyoroSetting {

	private static Object lock = new Object();
	public static final String TAG_CURRENT_CHARSET = "current charset";
	public static final String CURRENT_CHARSET_DEFAULT = "utf8";
	public static String getCurrentCharset() {
		String retry = CURRENT_CHARSET_DEFAULT;
		try {
			String t = getData(TAG_CURRENT_CHARSET);
			if (t != null && !t.equals("none")) {
				retry = t;
			}
		} catch (Throwable t) {
		}
		return retry;
	}

	public static void setCurrentCharset(String value) {
		try {
			setData(TAG_CURRENT_CHARSET, value);
		} catch (Throwable t) {
		}
	}


	public static void setData(String property, String value) {
		setData(KyoroApplication.getKyoroApplication(), property, value);
	}

	public static void setData(Context context, String property, String value) {
		setData(context, property, value, null);
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

	public static String getData(String property) {
		return getData(KyoroApplication.getKyoroApplication(), property);
	}

	public static String getData(Context context, String property) {
		return getData(context, property, null);
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
