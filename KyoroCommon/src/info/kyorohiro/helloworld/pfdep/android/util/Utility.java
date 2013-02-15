package info.kyorohiro.helloworld.pfdep.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Utility {
	public static boolean sIsDebug = false;
	public static boolean isDebuggingFromCash() {
		return sIsDebug;
	}
	public static boolean isDebugging(Context ctx){
		PackageManager manager = ctx.getPackageManager();
		ApplicationInfo appInfo = null;
		try {
			appInfo = manager.getApplicationInfo(ctx.getPackageName(), 0);
			if((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE) {
				sIsDebug = true;
			}
		} catch (NameNotFoundException e) {
			sIsDebug = false;
		}
		return isDebuggingFromCash();
	}
}
