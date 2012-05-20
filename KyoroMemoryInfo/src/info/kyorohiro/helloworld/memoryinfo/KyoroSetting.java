package info.kyorohiro.helloworld.memoryinfo;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KyoroSetting {

	public static final String VALUE_NONE = "none";
	public static final int CURRENT_FONT_SIZE_DEFAULT = 12;
	public static final String TAG_IS_FORGROUND = "forground";
	public static final String TAG_FILE_PATH = "forground";
	public static final String TAG_PACKAGE = "package";
	private static Object lock = new Object();
	private static HashMap<String, String> mCash = new HashMap<String, String>(); 

	public static boolean isForground() {
		boolean isForground = false;
		try{
			String t = getData(TAG_IS_FORGROUND);
			if(t != null && !t.equals(VALUE_NONE)){
				isForground = Boolean.parseBoolean(t);
			}
		}catch(Throwable t){
		}
		return isForground;
	}

	public static void isForground(boolean v) {
		try{
			setData(TAG_IS_FORGROUND, ""+v);
		} catch (Throwable t){
		}
	}



	public static void setData(String property, String value) {
		setData(KyoroApplication.getKyoroApplication(), property, value);
	}

	public static void setData(Context context, String property, String value) {
		setData(context, property, value, null);
	}

	public static String getData(String property) {
		return getData(KyoroApplication.getKyoroApplication(), property);
	}

	public static String getData(Context context, String property) {
		return getData(context, property, null);
	}

	public static String getData(Context context, String property, String tag) {
		SharedPreferences pref = null; 
		synchronized (lock){
			if(mCash.containsKey(property)){
				return mCash.get(property);
			}
			if (tag == null){
				pref = PreferenceManager.getDefaultSharedPreferences(context);
			}else{
				pref = context.getSharedPreferences(tag, Context.MODE_WORLD_READABLE);
			}
			String ret = pref.getString(property, VALUE_NONE);
			mCash.put(property, ret);
			return ret;
		}
	}

	public static void setData(Context context, String property, String value, String tag) {
		SharedPreferences pref = null; 
		synchronized (lock){
			mCash.put(property, value);
			if(tag == null){
				pref = PreferenceManager.getDefaultSharedPreferences(context);
			}else{
				pref = context.getSharedPreferences(tag, Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
			}
			pref.edit().putString(property, value).commit();
		}
	}
}
