package info.kyorohiro.helloworld.textviewer.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Util {

	public static double pixel2inchi(double from) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
	//	android.util.Log.v("kiyo","###"+metrics.xdpi+","+metrics.ydpi+","+metrics.density+","+metrics.scaledDensity);
		return from/((metrics.xdpi+metrics.ydpi)/2.0);///metrics.density);
	}

	public static double inchi2fontSize(double from) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
//		android.util.Log.v("kiyo","###"+metrics.xdpi+","+metrics.ydpi+","+metrics.density+","+metrics.scaledDensity);
		return from*metrics.densityDpi;
	}
	public static double inchi2pixel(double from) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
//		android.util.Log.v("kiyo","###"+metrics.xdpi+","+metrics.ydpi+","+metrics.density+","+metrics.scaledDensity);
		return from*((metrics.xdpi+metrics.ydpi)/2.0);//*metrics.density);
	}

	public static double inchi2mm(double from){
		return from*25.4;
	}


	public static double mm2inchi(double from){
		return (from/25.4);
	}

}
