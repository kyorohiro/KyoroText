package info.kyorohiro.helloworld.textviewer.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Util {

	public static double pixel2inchi(double from) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		return from/((metrics.xdpi+metrics.ydpi)/2);
	}

	public static double inchi2pixel(double from) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		return from*((metrics.xdpi+metrics.ydpi)/2);
	}

	public static double ‚‰nchi2mm(double from){
		return from*25.4;
	}


	public static double mm2‚‰nchi(double from){
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		//android.util.Log.d("test", "ret=" + (from/(metrics.densityDpi/160)/25.4));
		return (from/25.4);
	}

}
